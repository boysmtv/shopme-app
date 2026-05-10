package com.mtv.app.shopme.domain.model

data class NotificationPreferences(
    val orderNotification: Boolean,
    val promoNotification: Boolean,
    val chatNotification: Boolean,
    val pushEnabled: Boolean,
    val emailEnabled: Boolean
) {
    fun hasAnyEnabled(): Boolean {
        return orderNotification || promoNotification || chatNotification || pushEnabled || emailEnabled
    }
}
