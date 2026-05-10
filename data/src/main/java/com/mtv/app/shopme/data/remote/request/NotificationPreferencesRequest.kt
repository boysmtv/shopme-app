package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class NotificationPreferencesRequest(
    val orderNotification: Boolean,
    val promoNotification: Boolean,
    val chatNotification: Boolean,
    val pushEnabled: Boolean,
    val emailEnabled: Boolean
)
