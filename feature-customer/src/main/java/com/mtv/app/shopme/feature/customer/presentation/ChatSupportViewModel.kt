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
import com.mtv.app.shopme.feature.customer.contract.ChatSupportEffect
import com.mtv.app.shopme.feature.customer.contract.ChatSupportEvent
import com.mtv.app.shopme.feature.customer.contract.ChatSupportUiState
import com.mtv.app.shopme.feature.customer.contract.SupportMessage
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
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
class ChatSupportViewModel @Inject constructor() :
    BaseEventViewModel<ChatSupportEvent, ChatSupportEffect>() {

    private val _state = MutableStateFlow(
        ChatSupportUiState(
            messages = listOf(
                SupportMessage(
                    id = "1",
                    message = "Halo 👋 Ada yang bisa kami bantu?",
                    isFromUser = false,
                    timestamp = "10:00"
                )
            )
        )
    )
    val uiState = _state.asStateFlow()

    override fun onEvent(event: ChatSupportEvent) {
        when (event) {
            is ChatSupportEvent.Load -> {}
            is ChatSupportEvent.DismissDialog -> dismissDialog()

            is ChatSupportEvent.OnMessageChange -> onMessageChange(event.value)
            is ChatSupportEvent.SendMessage -> sendMessage()

            is ChatSupportEvent.ClickBack -> emitEffect(ChatSupportEffect.NavigateBack)
        }
    }

    private fun onMessageChange(value: String) {
        _state.update {
            it.copy(currentMessage = value)
        }
    }

    private fun sendMessage() {
        val message = _state.value.currentMessage
        if (message.isBlank()) return

        val newMessage = SupportMessage(
            id = System.currentTimeMillis().toString(),
            message = message,
            isFromUser = true,
            timestamp = "Now"
        )
        val encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
        val uri = "https://wa.me/6281234567890?text=$encodedMessage".toUri()

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
