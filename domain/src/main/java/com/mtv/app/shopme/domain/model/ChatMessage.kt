package com.mtv.app.shopme.domain.model

data class ChatMessage(
    val id: String,
    val message: String,
    val time: String,
    val isFromUser: Boolean,
    val isRead: Boolean = true,
    val isPending: Boolean = false,
    val isFailed: Boolean = false
)
