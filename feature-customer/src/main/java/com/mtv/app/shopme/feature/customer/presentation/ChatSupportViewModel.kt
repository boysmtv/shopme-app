/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatSupportViewModel.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 11.13
 */

package com.mtv.app.shopme.feature.customer.presentation

import android.content.Intent
import androidx.core.net.toUri
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.SupportCenter
import com.mtv.app.shopme.domain.usecase.GetSupportCenterUseCase
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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ChatSupportViewModel @Inject constructor(
    private val getSupportCenterUseCase: GetSupportCenterUseCase,
    private val sessionManager: SessionManager
) :
    BaseEventViewModel<ChatSupportEvent, ChatSupportEffect>() {

    private val _state = MutableStateFlow(ChatSupportUiState())
    val uiState = _state.asStateFlow()

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
        observeDataFlow(
            flow = getSupportCenterUseCase(),
            onState = { state ->
                _state.update { current ->
                    when (state) {
                        is LoadState.Success -> state.data.toUiState()
                        else -> current.copy(isLoading = state is LoadState.Loading)
                    }
                }
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
        val message = _state.value.currentMessage
        val whatsapp = _state.value.whatsapp
        if (message.isBlank() || whatsapp.isBlank()) return

        val newMessage = SupportMessage(
            id = System.currentTimeMillis().toString(),
            message = message,
            isFromUser = true,
            timestamp = "Now"
        )
        val encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
        val uri = "https://wa.me/$whatsapp?text=$encodedMessage".toUri()

        _state.update {
            it.copy(
                messages = it.messages + newMessage,
                currentMessage = "",
                isAgentTyping = false,
                sendMessage = LoadState.Success(Unit)
            )
        }

        emitEffect(
            ChatSupportEffect.OpenIntent(
                Intent(Intent.ACTION_VIEW, uri)
            )
        )
    }

    private fun SupportCenter.toUiState() = ChatSupportUiState(
        isLoading = false,
        title = liveChatTitle,
        statusLabel = liveChatStatusOnlineLabel,
        whatsapp = whatsapp,
        messages = bootstrapMessages.map {
            SupportMessage(
                id = it.id,
                message = it.message,
                isFromUser = it.isFromUser,
                timestamp = it.timestamp
            )
        },
        currentMessage = "",
        isAgentTyping = false,
        sendMessage = LoadState.Idle
    )

    private fun showError(error: UiError) {
        handleSessionError(
            error = error,
            sessionManager = sessionManager,
            beforeLogout = { _state.update { it.copy(isLoading = false) } }
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
