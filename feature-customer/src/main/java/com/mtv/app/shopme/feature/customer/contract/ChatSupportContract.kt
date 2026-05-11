/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatSupportContract.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 11.12
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.based.core.network.utils.LoadState

data class ChatSupportUiState(
    val isLoading: Boolean = false,
    val title: String = "",
    val statusLabel: String = "",
    val messages: List<SupportMessage> = emptyList(),
    val currentMessage: String = "",
    val isAgentTyping: Boolean = false,

    val sendMessage: LoadState<Unit> = LoadState.Idle
)

sealed class ChatSupportEvent {
    object Load : ChatSupportEvent()
    object DismissDialog : ChatSupportEvent()

    data class OnMessageChange(val value: String) : ChatSupportEvent()
    object SendMessage : ChatSupportEvent()

    object ClickBack : ChatSupportEvent()
}

sealed class ChatSupportEffect {
    object NavigateBack : ChatSupportEffect()
}

data class SupportMessage(
    val id: String,
    val message: String,
    val isFromUser: Boolean,
    val timestamp: String
)
