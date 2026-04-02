/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.45
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.usecase.ChatMessageMarkAsReadUseCase
import com.mtv.app.shopme.domain.usecase.CreateChatMessageSendUseCase
import com.mtv.app.shopme.domain.usecase.GetChatMessageUseCase
import com.mtv.app.shopme.feature.customer.contract.ChatEffect
import com.mtv.app.shopme.feature.customer.contract.ChatEvent
import com.mtv.app.shopme.feature.customer.contract.ChatUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatMessageUseCase: GetChatMessageUseCase,
    private val chatSendMessageUseCase: CreateChatMessageSendUseCase,
    private val chatMessageMarkAsReadUseCase: ChatMessageMarkAsReadUseCase,
) : BaseEventViewModel<ChatEvent, ChatEffect>() {

    private val _state = MutableStateFlow(ChatUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.Load -> load()
            is ChatEvent.DismissDialog -> dismissDialog()
            is ChatEvent.SendMessage -> sendMessage(event)
            is ChatEvent.ReadAllMessage -> readAll(event)
            is ChatEvent.ClickBack -> emitEffect(ChatEffect.NavigateBack)
        }
    }

    private fun load() {
        observeChat()
    }

    private fun observeChat() {
        observeDataFlow(
            flow = chatMessageUseCase(),
            onState = { state ->
                _state.update {
                    it.copy(chats = state)
                }
            },
            onError = { showError(it) }
        )
    }

    private fun sendMessage(event: ChatEvent.SendMessage) {
        observeDataFlow(
            flow = chatSendMessageUseCase(event.id, event.message),
            onState = { state ->
                _state.update {
                    it.copy(sendMessage = state)
                }
            },
            onError = { showError(it) }
        )
    }

    private fun readAll(event: ChatEvent.ReadAllMessage) {
        observeDataFlow(
            flow = chatMessageMarkAsReadUseCase(event.id),
            onState = { state ->
                _state.update {
                    it.copy(readAll = state)
                }
            },
            onError = { showError(it) }
        )
    }

    private fun showError(error: UiError) {
        setDialog(
            UiDialog.Center(
                state = DialogStateV1(
                    type = DialogType.ERROR,
                    title = ErrorMessages.GENERIC_ERROR,
                    message = error.message
                ),
                onPrimary = { dismissDialog() }
            )
        )
    }
}