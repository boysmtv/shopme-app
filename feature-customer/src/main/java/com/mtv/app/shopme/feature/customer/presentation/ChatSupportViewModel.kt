/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatSupportViewModel.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 11.13
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.SupportChat
import com.mtv.app.shopme.domain.usecase.GetSupportChatUseCase
import com.mtv.app.shopme.domain.usecase.GetSupportCenterUseCase
import com.mtv.app.shopme.domain.usecase.SendSupportChatMessageUseCase
import com.mtv.app.shopme.feature.customer.contract.ChatSupportEffect
import com.mtv.app.shopme.feature.customer.contract.ChatSupportEvent
import com.mtv.app.shopme.feature.customer.contract.ChatSupportUiState
import com.mtv.app.shopme.feature.customer.contract.SupportMessage
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SessionManager
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import com.mtv.based.core.network.utils.ErrorMessages
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ChatSupportViewModel @Inject constructor(
    private val getSupportCenterUseCase: GetSupportCenterUseCase,
    private val getSupportChatUseCase: GetSupportChatUseCase,
    private val sendSupportChatMessageUseCase: SendSupportChatMessageUseCase,
    private val sessionManager: SessionManager
) :
    BaseEventViewModel<ChatSupportEvent, ChatSupportEffect>() {

    private val _state = MutableStateFlow(ChatSupportUiState())
    val uiState = _state.asStateFlow()
    private var isLoadingCenter = false
    private var isLoadingMessages = false

    override fun onEvent(event: ChatSupportEvent) {
        when (event) {
            is ChatSupportEvent.Load -> load()
            is ChatSupportEvent.DismissDialog -> dismissDialog()

            is ChatSupportEvent.OnMessageChange -> onMessageChange(event.value)
            is ChatSupportEvent.SendMessage -> sendMessage()

            is ChatSupportEvent.ClickBack -> emitEffect(ChatSupportEffect.NavigateBack)
        }
    }

    private fun load() {
        loadSupportCenter()
        loadMessages()
    }

    private fun loadSupportCenter() {
        observeIndependentDataFlow(
            flow = getSupportCenterUseCase(),
            onLoad = {
                isLoadingCenter = true
                syncLoading()
            },
            onSuccess = { support ->
                isLoadingCenter = false
                _state.update { current ->
                    current.copy(
                        title = support.liveChatTitle,
                        statusLabel = support.liveChatStatusOnlineLabel
                    )
                }
                syncLoading()
            },
            onError = ::showError
        )
    }

    private fun loadMessages() {
        observeIndependentDataFlow(
            flow = getSupportChatUseCase(),
            onLoad = {
                isLoadingMessages = true
                syncLoading()
            },
            onSuccess = { chat ->
                isLoadingMessages = false
                _state.update { current ->
                    current.copy(
                        messages = chat.toUiMessages(),
                        isAgentTyping = false,
                        sendMessage = LoadState.Idle
                    )
                }
                syncLoading()
            },
            onError = ::showError
        )
    }

    private fun onMessageChange(value: String) {
        _state.update {
            it.copy(currentMessage = value)
        }
    }

    private fun sendMessage() {
        val message = _state.value.currentMessage.trim()
        if (message.isBlank()) return

        observeIndependentDataFlow(
            flow = sendSupportChatMessageUseCase(message),
            onLoad = {
                _state.update {
                    it.copy(
                        isAgentTyping = true,
                        sendMessage = LoadState.Loading
                    )
                }
            },
            onSuccess = { chat ->
                _state.update {
                    it.copy(
                        messages = chat.toUiMessages(),
                        currentMessage = "",
                        isAgentTyping = false,
                        sendMessage = LoadState.Success(Unit)
                    )
                }
            },
            onError = ::showError
        )
    }

    private fun SupportChat.toUiMessages(): List<SupportMessage> = messages.map {
        SupportMessage(
            id = it.id,
            message = it.message,
            isFromUser = it.isFromUser,
            timestamp = it.timestamp
        )
    }

    private fun syncLoading() {
        _state.update { it.copy(isLoading = isLoadingCenter || isLoadingMessages) }
    }

    private fun showError(error: UiError) {
        isLoadingCenter = false
        isLoadingMessages = false
        handleSessionError(
            error = error,
            sessionManager = sessionManager,
            beforeLogout = { _state.update { it.copy(isLoading = false, isAgentTyping = false) } }
        ) {
            syncLoading()
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
            _state.update {
                it.copy(
                    isAgentTyping = false,
                    sendMessage = LoadState.Error(error)
                )
            }
        }
    }
}
