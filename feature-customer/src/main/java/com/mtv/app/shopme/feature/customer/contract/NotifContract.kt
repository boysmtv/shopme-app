/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotifContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 14.56
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.feature.customer.firebase.NotifItem
import com.mtv.based.core.network.utils.ResourceFirebase
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.ERROR_STRING

data class NotifStateListener(
    val notificationState: ResourceFirebase<String> = ResourceFirebase.Loading,
    val activeDialog: NotifDialog? = null
)

data class NotifDataListener(
    val localNotification: List<NotifItem> = emptyList(),
)

data class NotifEventListener(
    val onNotificationClicked: (item: NotifItem) -> Unit,
    val onGetNotification: () -> Unit,
    val onClearNotification: () -> Unit,
    val onDismissActiveDialog: () -> Unit,
)

data class NotifNavigationListener(
    val onBack: () -> Unit,
)

sealed class NotifDialog {
    data class Error(
        val message: String = ERROR_STRING
    ) : NotifDialog()

    object Success : NotifDialog()
}