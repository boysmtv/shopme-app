package com.mtv.app.shopme.domain.model

data class SupportChat(
    val messages: List<SupportChatMessage>
)

data class SupportChatMessage(
    val id: String,
    val message: String,
    val isFromUser: Boolean,
    val timestamp: String
)
