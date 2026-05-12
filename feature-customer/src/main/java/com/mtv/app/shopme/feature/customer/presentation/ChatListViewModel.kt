/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ListChatViewModel.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 13.34
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEventType
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeGateway
import com.mtv.app.shopme.domain.model.ChatList
import com.mtv.app.shopme.domain.usecase.ClearChatListUseCase
import com.mtv.app.shopme.domain.usecase.GetChatListUseCase
import com.mtv.app.shopme.feature.customer.contract.ChatListEffect
import com.mtv.app.shopme.feature.customer.contract.ChatListEffect.*
import com.mtv.app.shopme.feature.customer.contract.ChatListEvent
import com.mtv.app.shopme.feature.customer.contract.ChatListUiState
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
class ChatListViewModel @Inject constructor(
    private val chatListUseCase: GetChatListUseCase,
    private val clearChatListUseCase: ClearChatListUseCase,
    private val realtimeGateway: ShopmeRealtimeGateway,
    private val sessionManager: SessionManager
) : BaseEventViewModel<ChatListEvent, ChatListEffect>() {

    private val _state = MutableStateFlow(ChatListUiState())
    val uiState = _state.asStateFlow()

    init {
        viewModelScope.launch {
            realtimeGateway.events.collectLatest { event ->
                when (event.type) {
                    ShopmeRealtimeEventType.CHAT_MESSAGE -> {
                        if (!applyMessageDelta(event.conversationId, event.message, event.occurredAt)) {
                            observeChatList()
                        }
                    }
                    ShopmeRealtimeEventType.CHAT_READ -> {
                        if (!applyReadDelta(event.conversationId)) {
                            observeChatList()
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onEvent(event: ChatListEvent) {
        when (event) {
            is ChatListEvent.Load -> observeChatList()
            is ChatListEvent.ClickClearAll -> clearChats()
            is ChatListEvent.DismissDialog -> dismissDialog()
            is ChatListEvent.ClickItem -> emitEffect(NavigateToChat(event.id))
            is ChatListEvent.ClickBack -> emitEffect(NavigateBack)
        }
    }

    private fun observeChatList() {
        realtimeGateway.ensureConnected()
        observeDataFlow(
            flow = chatListUseCase(),
            onLoad = ::showLoading,
            onSuccess = { hideLoading() },
            onState = { state ->
                _state.update {
                    it.copy(chatListState = state)
                }
            },
            onError = {
                hideLoading()
                showError(it)
            }
        )
    }

    private fun clearChats() {
        observeIndependentDataFlow(
            flow = clearChatListUseCase(),
            onLoad = ::showLoading,
            onSuccess = {
                hideLoading()
                _state.update {
                    it.copy(chatListState = LoadState.Success(ChatList(emptyList())))
                }
            },
            onError = { error ->
                hideLoading()
                showError(error)
            }
        )
    }

    private fun applyMessageDelta(
        conversationId: String?,
        message: String?,
        occurredAt: String?
    ): Boolean {
        val targetId = conversationId?.takeIf { it.isNotBlank() } ?: return false
        val currentState = _state.value.chatListState as? LoadState.Success ?: return false
        val current = currentState.data.chatList
        val existing = current.firstOrNull { it.id == targetId } ?: return false
        val updated = existing.copy(
            lastMessage = message.orEmpty().ifBlank { existing.lastMessage },
            time = occurredAt.toRealtimeChatTime(existing.time)
        )

        _state.update {
            it.copy(
                chatListState = LoadState.Success(
                    ChatList(
                        chatList = listOf(updated) + current.filterNot { item -> item.id == targetId }
                    )
                )
            )
        }
        return true
    }

    private fun applyReadDelta(conversationId: String?): Boolean {
        val targetId = conversationId?.takeIf { it.isNotBlank() } ?: return false
        val currentState = _state.value.chatListState as? LoadState.Success ?: return false
        val current = currentState.data.chatList
        if (current.none { it.id == targetId }) return false

        _state.update {
            it.copy(
                chatListState = LoadState.Success(
                    ChatList(
                        chatList = current.map { item ->
                            if (item.id == targetId) item.copy(unreadCount = 0) else item
                        }
                    )
                )
            )
        }
        return true
    }

    private fun String?.toRealtimeChatTime(fallback: String): String =
        this?.substringAfter("T", "")
            ?.take(5)
            ?.takeIf { it.length == 5 }
            ?: fallback

    private fun showError(error: UiError) {
        handleSessionError(
            error = error,
            sessionManager = sessionManager,
            beforeLogout = { hideLoading() }
        ) {
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
