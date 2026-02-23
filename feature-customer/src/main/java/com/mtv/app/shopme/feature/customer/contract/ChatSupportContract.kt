/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatSupportContract.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 11.12
 */

package com.mtv.app.shopme.feature.customer.contract

data class ChatSupportStateListener(
    val isLoading: Boolean = false,
    val isSending: Boolean = false
)

data class ChatSupportDataListener(
    val messages: List<SupportMessage> = emptyList(),
    val currentMessage: String = "",
    val isAgentTyping: Boolean = false
)

data class SupportMessage(
    val id: String,
    val message: String,
    val isFromUser: Boolean,
    val timestamp: String
)

data class ChatSupportEventListener(
    val onMessageChange: (String) -> Unit = {},
    val onSendMessage: () -> Unit = {}
)

data class ChatSupportNavigationListener(
    val onBack: () -> Unit = {}
)