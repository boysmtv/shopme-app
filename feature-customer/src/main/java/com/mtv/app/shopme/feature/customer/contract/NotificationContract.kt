/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotificationContract.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.26
 */

package com.mtv.app.shopme.feature.customer.contract

data class NotificationStateListener(
    val loading: Boolean = false
)

data class NotificationDataListener(
    val orderNotification: Boolean = true,
    val promoNotification: Boolean = true,
    val chatNotification: Boolean = true,
    val pushEnabled: Boolean = true,
    val emailEnabled: Boolean = false
)

data class NotificationEventListener(
    val onToggleOrder: (Boolean) -> Unit = {},
    val onTogglePromo: (Boolean) -> Unit = {},
    val onToggleChat: (Boolean) -> Unit = {},
    val onTogglePush: (Boolean) -> Unit = {},
    val onToggleEmail: (Boolean) -> Unit = {}
)

data class NotificationNavigationListener(
    val onBack: () -> Unit = {}
)