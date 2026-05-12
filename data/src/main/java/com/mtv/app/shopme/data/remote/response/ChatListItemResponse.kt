@file:OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class ChatListItemResponse(
    val id: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    @JsonNames("avatarBase64")
    val avatar: String? = null
)
