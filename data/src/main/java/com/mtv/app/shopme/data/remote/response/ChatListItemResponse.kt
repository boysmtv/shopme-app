package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatListItemResponse(
    val id: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val avatarBase64: String? = null
)