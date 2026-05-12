/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatDetailViewModel.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.32
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEventType
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeGateway
import com.mtv.app.shopme.domain.usecase.ChatMessageMarkAsReadUseCase
import com.mtv.app.shopme.domain.usecase.CreateChatMessageSendUseCase
import com.mtv.app.shopme.domain.usecase.GetChatListUseCase
import com.mtv.app.shopme.domain.usecase.GetChatMessageUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailEffect
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailEvent
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailMessage
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
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

@HiltViewModel
class SellerChatDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getChatListUseCase: GetChatListUseCase,
    private val getChatMessageUseCase: GetChatMessageUseCase,
    private val sendChatMessageUseCase: CreateChatMessageSendUseCase,
    private val chatMessageMarkAsReadUseCase: ChatMessageMarkAsReadUseCase,
    private val realtimeGateway: ShopmeRealtimeGateway,
    private val sessionManager: SessionManager,
) : BaseEventViewModel<SellerChatDetailEvent, SellerChatDetailEffect>() {

    private val routeChatId: String = savedStateHandle.get<String>("chatId").orEmpty()
    private var lastReadChatId: String? = null

    private val _state = MutableStateFlow(
        SellerChatDetailUiState(
            activeChatId = routeChatId
        )
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
            }
        }
    }

    override fun onEvent(event: SellerChatDetailEvent) {
        when (event) {
            is SellerChatDetailEvent.Load -> load()
            is SellerChatDetailEvent.DismissDialog -> dismissDialog()

            is SellerChatDetailEvent.ChangeMessage -> {
                _state.update { it.copy(currentMessage = event.value) }
            }

            is SellerChatDetailEvent.SendMessage -> sendMessage()

            is SellerChatDetailEvent.ClickBack ->
                emitEffect(SellerChatDetailEffect.NavigateBack)
        }
    }

    private fun load() {
        _state.update { it.copy(isLoading = true) }
        realtimeGateway.ensureConnected()
        observeChatMetadata()
    }

    private fun observeChatMetadata() {
        observeDataFlow(
            flow = getChatListUseCase(asSeller = true),
            onState = { state ->
                val items = (state as? LoadState.Success)
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
                        isLoading = false,
                        activeChatId = activeChat?.id.orEmpty(),
                        chatName = activeChat?.name.orEmpty(),
                        chatAvatarUrl = activeChat?.avatarUrl
                    )
                }
                observeChat(_state.value.activeChatId.ifBlank { routeChatId }.ifBlank { null })
            },
            onError = ::showError
        )
    }

    private fun observeChat(chatId: String? = _state.value.activeChatId.ifBlank { routeChatId }.ifBlank { null }) {
        observeDataFlow(
            flow = getChatMessageUseCase(chatId, asSeller = true),
            onState = { state ->
                var nextActiveChatId = _state.value.activeChatId
                _state.update {
                    val activeId = (state as? LoadState.Success)
                        ?.data
                        ?.firstOrNull()
                        ?.id
                        .orEmpty()
                    val messages = (state as? LoadState.Success)
                        ?.data
                        ?.map { item ->
                            SellerChatDetailMessage(
                                message = item.lastMessage,
                                isFromSeller = !item.isFromUser
                            )
                        }
                        .orEmpty()

                    it.copy(
                        isLoading = state is LoadState.Loading,
                        activeChatId = it.activeChatId.ifBlank { activeId },
                        messages = if (messages.isNotEmpty()) messages else it.messages
                    ).also { nextState ->
                        nextActiveChatId = nextState.activeChatId
                    }
                }
                markAsReadIfNeeded(nextActiveChatId)
            },
            onError = ::showError
        )
    }

    private fun sendMessage() {
        val message = _state.value.currentMessage
        if (message.isBlank()) return
        val chatId = _state.value.activeChatId
        if (chatId.isBlank()) return

        observeDataFlow(
            flow = sendChatMessageUseCase(chatId, message, asSeller = true),
            onState = { state ->
                _state.update { it.copy(isSending = state is LoadState.Loading) }
            },
            onSuccess = {
                _state.update { it.copy(currentMessage = "", isSending = false) }
                observeChatMetadata()
                observeChat()
            },
            onError = ::showError
        )
    }

    private fun readAll(chatId: String) {
        observeDataFlow(
            flow = chatMessageMarkAsReadUseCase(chatId, asSeller = true),
            onError = ::showError
        )
    }

    private fun markAsReadIfNeeded(chatId: String) {
        if (chatId.isBlank() || lastReadChatId == chatId) return
        lastReadChatId = chatId
        readAll(chatId)
    }

    private fun showError(error: UiError) {
        handleSessionError(error, sessionManager) {
            _state.update { it.copy(isLoading = false, isSending = false) }
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
}
