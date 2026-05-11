package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class SupportChatResponse(
    val messages: List<SupportChatMessageResponse> = emptyList()
)

@Serializable
data class SupportChatMessageResponse(
    val id: String,
    val message: String,
    val isFromUser: Boolean,
    val timestamp: String
)
