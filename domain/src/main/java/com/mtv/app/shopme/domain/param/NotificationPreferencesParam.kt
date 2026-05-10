package com.mtv.app.shopme.domain.param

data class NotificationPreferencesParam(
    val orderNotification: Boolean,
    val promoNotification: Boolean,
    val chatNotification: Boolean,
    val pushEnabled: Boolean,
    val emailEnabled: Boolean
)
