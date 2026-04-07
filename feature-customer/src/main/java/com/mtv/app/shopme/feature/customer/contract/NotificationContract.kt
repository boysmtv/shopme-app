/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotificationContract.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.26
 */

package com.mtv.app.shopme.feature.customer.contract

data class NotificationUiState(
    val loading: Boolean = false,

    val orderNotification: Boolean = true,
    val promoNotification: Boolean = true,
    val chatNotification: Boolean = true,
    val pushEnabled: Boolean = true,
    val emailEnabled: Boolean = false
)

sealed class NotificationEvent {
    object Load : NotificationEvent()
    object DismissDialog : NotificationEvent()
    data class ToggleOrder(val value: Boolean) : NotificationEvent()
    data class TogglePromo(val value: Boolean) : NotificationEvent()
    data class ToggleChat(val value: Boolean) : NotificationEvent()
    data class TogglePush(val value: Boolean) : NotificationEvent()
    data class ToggleEmail(val value: Boolean) : NotificationEvent()

    object ClickBack : NotificationEvent()
}

sealed class NotificationEffect {
    object NavigateBack : NotificationEffect()
}