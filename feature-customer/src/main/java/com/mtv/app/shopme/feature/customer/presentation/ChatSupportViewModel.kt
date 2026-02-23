/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatSupportViewModel.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 11.13
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.customer.contract.ChatSupportDataListener
import com.mtv.app.shopme.feature.customer.contract.ChatSupportStateListener
import com.mtv.app.shopme.feature.customer.contract.SupportMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ChatSupportViewModel @Inject constructor() :
    BaseViewModel(),
    UiOwner<ChatSupportStateListener, ChatSupportDataListener> {

    override val uiState = MutableStateFlow(ChatSupportStateListener())

    override val uiData = MutableStateFlow(
        ChatSupportDataListener(
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

    fun onMessageChange(value: String) {
        uiData.value = uiData.value.copy(currentMessage = value)
    }

    fun sendMessage() {
        val message = uiData.value.currentMessage
        if (message.isBlank()) return

        val newMessage = SupportMessage(
            id = System.currentTimeMillis().toString(),
            message = message,
            isFromUser = true,
            timestamp = "Now"
        )

        uiData.value = uiData.value.copy(
            messages = uiData.value.messages + newMessage,
            currentMessage = "",
            isAgentTyping = true
        )

        simulateAgentReply()
    }

    private fun simulateAgentReply() {
        viewModelScope.launch {
            delay(1500)
            uiData.value = uiData.value.copy(
                messages = uiData.value.messages + SupportMessage(
                    id = System.currentTimeMillis().toString(),
                    message = "Terima kasih atas pesan Anda 🙌 Tim kami sedang memproses.",
                    isFromUser = false,
                    timestamp = "Now"
                ),
                isAgentTyping = false
            )
        }
    }
}