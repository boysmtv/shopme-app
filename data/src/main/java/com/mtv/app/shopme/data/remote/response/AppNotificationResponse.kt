package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class AppNotificationResponse(
    val id: String,
    val title: String,
    val message: String,
    val data: Map<String, String> = emptyMap(),
    val isRead: Boolean,
    val createdAt: String
)
