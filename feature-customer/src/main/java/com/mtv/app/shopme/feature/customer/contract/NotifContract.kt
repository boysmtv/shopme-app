/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotifContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 14.56
 */
package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.data.local.NotificationItem
import com.mtv.based.core.network.utils.ResourceFirebase

data class NotifUiState(
    val localNotification: List<NotificationItem> = emptyList(),

    val notificationState: ResourceFirebase<String> = ResourceFirebase.Loading,
    val activeDialog: NotifDialog? = null
)

sealed class NotifEvent {
    object Load : NotifEvent()
    object DismissDialog : NotifEvent()

    object GetNotification : NotifEvent()
    object ClearNotification : NotifEvent()

    data class ClickNotification(val item: NotificationItem) : NotifEvent()

    object ClickBack : NotifEvent()
}

sealed class NotifEffect {
    object NavigateBack : NotifEffect()
}

sealed class NotifDialog {
    data class Error(
        val message: String
    ) : NotifDialog()

    object Success : NotifDialog()
}