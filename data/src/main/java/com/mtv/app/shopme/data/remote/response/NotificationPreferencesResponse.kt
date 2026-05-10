package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class NotificationPreferencesResponse(
    val orderNotification: Boolean,
    val promoNotification: Boolean,
    val chatNotification: Boolean,
    val pushEnabled: Boolean,
    val emailEnabled: Boolean
)
