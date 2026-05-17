/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.45
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEvent
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEventType
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeGateway
import com.mtv.app.shopme.domain.usecase.ChatMessageMarkAsReadUseCase
import com.mtv.app.shopme.domain.usecase.CreateChatMessageSendUseCase
import com.mtv.app.shopme.domain.usecase.GetChatListUseCase
import com.mtv.app.shopme.domain.usecase.GetChatMessageUseCase
import com.mtv.app.shopme.domain.model.ChatListItem
import com.mtv.app.shopme.feature.customer.contract.ChatEffect
import com.mtv.app.shopme.feature.customer.contract.ChatEvent
import com.mtv.app.shopme.feature.customer.contract.ChatUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SessionManager
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatListUseCase: GetChatListUseCase,
    private val chatMessageUseCase: GetChatMessageUseCase,
    private val chatSendMessageUseCase: CreateChatMessageSendUseCase,
    private val chatMessageMarkAsReadUseCase: ChatMessageMarkAsReadUseCase,
    private val realtimeGateway: ShopmeRealtimeGateway,
    private val sessionManager: SessionManager,
) : BaseEventViewModel<ChatEvent, ChatEffect>() {

    private val routeChatId: String = savedStateHandle.get<String>("chatId").orEmpty()
    private var lastReadChatId: String? = null

    private val _state = MutableStateFlow(
        ChatUiState(activeChatId = routeChatId)
    )
    val uiState = _state.asStateFlow()

    init {
        viewModelScope.launch {
            realtimeGateway.events.collectLatest { event ->
                when (event.type) {
                    ShopmeRealtimeEventType.CHAT_MESSAGE,
                    ShopmeRealtimeEventType.CHAT_READ -> {
                        observeChatMetadata()
                    }

                    else -> Unit
                }
                if (event.type == ShopmeRealtimeEventType.PRESENCE_CHANGED) {
                    handlePresence(event)
                }
            }
        }
    }

    override fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.Load -> load()
            is ChatEvent.DismissDialog -> dismissDialog()
            is ChatEvent.SendMessage -> sendMessage(event)
            is ChatEvent.RetryMessage -> retryMessage(event)
            is ChatEvent.ReadAllMessage -> readAll(event)
            is ChatEvent.ClickBack -> emitEffect(ChatEffect.NavigateBack)
        }
    }

    private fun load() {
        realtimeGateway.ensureConnected()
        observeChatMetadata()
        observeChat()
    }

    private fun observeChatMetadata() {
        observeIndependentDataFlow(
            flow = chatListUseCase(),
            onState = { state ->
                val items = (state as? com.mtv.based.core.network.utils.LoadState.Success)
                    ?.data
                    ?.chatList
                    .orEmpty()
                _state.update {
                    val resolvedActiveId = when {
                        it.activeChatId.isBlank() -> items.firstOrNull()?.id.orEmpty()
                        items.any { item -> item.id == it.activeChatId } -> it.activeChatId
                        else -> items.firstOrNull()?.id.orEmpty()
                    }
                    val activeChat = items.firstOrNull { item -> item.id == resolvedActiveId }
                        ?: items.firstOrNull()
                    it.copy(
                        activeChatId = activeChat?.id.orEmpty(),
                        chatName = activeChat?.name.orEmpty(),
                        chatAvatarUrl = activeChat?.avatarUrl
                    )
                }
                observeChat(_state.value.activeChatId.ifBlank { routeChatId }.ifBlank { null })
            },
            onError = { showError(it) }
        )
    }

    private fun observeChat(chatId: String? = _state.value.activeChatId.ifBlank { routeChatId }.ifBlank { null }) {
        observeIndependentDataFlow(
            flow = chatMessageUseCase(chatId),
            onState = { state ->
                var nextActiveChatId = _state.value.activeChatId
                _state.update {
                    if (state is com.mtv.based.core.network.utils.LoadState.Loading &&
                        it.chats is com.mtv.based.core.network.utils.LoadState.Success
                    ) {
                        return@update it.copy(isRefreshing = true)
                    }

                    val activeId = (state as? com.mtv.based.core.network.utils.LoadState.Success)
                        ?.data
                        ?.firstOrNull()
                        ?.id
                        .orEmpty()

                    it.copy(
                        activeChatId = it.activeChatId.ifBlank { activeId },
                        chats = state,
                        optimisticMessages = if (state is com.mtv.based.core.network.utils.LoadState.Success) {
                            emptyList()
                        } else {
                            it.optimisticMessages
                        },
                        isRefreshing = false
                    ).also { nextState ->
                        nextActiveChatId = nextState.activeChatId
                    }
                }
                markAsReadIfNeeded(nextActiveChatId)
            },
            onError = { showError(it) }
        )
    }

    private fun sendMessage(event: ChatEvent.SendMessage) {
        val activeChatId = _state.value.activeChatId.ifBlank { event.id }
        if (activeChatId.isBlank()) return
        val localMessage = ChatListItem(
            id = "local-${System.currentTimeMillis()}",
            name = "",
            lastMessage = event.message,
            time = currentChatTime(),
            unreadCount = 0,
            avatarUrl = null,
            isFromUser = true,
            isPending = true,
            isRead = false,
            isFailed = false
        )
        _state.update {
            it.copy(
                optimisticMessages = it.optimisticMessages + localMessage
            )
        }
        observeIndependentDataFlow(
            flow = chatSendMessageUseCase(activeChatId, event.message),
            onState = { state ->
                _state.update {
                    it.copy(sendMessage = state)
                }
            },
            onSuccess = {
                observeChatMetadata()
                observeChat()
            },
            onError = {
                _state.update { state ->
                    state.copy(
                        optimisticMessages = state.optimisticMessages.map { message ->
                            if (message.id == localMessage.id) {
                                message.copy(isPending = false, isRead = false, isFailed = true)
                            } else {
                                message
                            }
                        }
                    )
                }
                showError(it)
            }
        )
    }

    private fun retryMessage(event: ChatEvent.RetryMessage) {
        val activeChatId = _state.value.activeChatId
        if (activeChatId.isBlank() || event.message.isBlank()) return
        _state.update {
            it.copy(optimisticMessages = it.optimisticMessages.filterNot { message -> message.id == event.localId })
        }
        sendMessage(ChatEvent.SendMessage(activeChatId, event.message))
    }

    private fun readAll(event: ChatEvent.ReadAllMessage) {
        observeIndependentDataFlow(
            flow = chatMessageMarkAsReadUseCase(event.id),
            onState = { state ->
                _state.update {
                    it.copy(readAll = state)
                }
            },
            onError = { showError(it) }
        )
    }

    private fun markAsReadIfNeeded(chatId: String) {
        if (chatId.isBlank() || lastReadChatId == chatId) return
        lastReadChatId = chatId
        observeIndependentDataFlow(
            flow = chatMessageMarkAsReadUseCase(chatId),
            onError = { showError(it) }
        )
    }

    private fun handlePresence(event: ShopmeRealtimeEvent) {
        val activeCafeId = _state.value.activeChatId.conversationCafeId()
        if (activeCafeId.isBlank() || event.onlineCafeId != activeCafeId) {
            return
        }
        _state.update {
            it.copy(
                isPeerOnline = event.online == true,
                peerLastSeenAt = event.lastSeenAt.orEmpty()
            )
        }
    }

    private fun showError(error: UiError) {
        handleSessionError(error, sessionManager) {
            setDialog(
                UiDialog.Center(
                    state = DialogStateV1(
                        type = DialogType.ERROR,
                        title = ErrorMessages.GENERIC_ERROR,
                        message = it.message
                    ),
                    onPrimary = { dismissDialog() }
                )
            )
        }
    }

    private fun currentChatTime(): String =
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
}

private fun String.conversationCafeId(): String = substringBefore("_", missingDelimiterValue = "")
