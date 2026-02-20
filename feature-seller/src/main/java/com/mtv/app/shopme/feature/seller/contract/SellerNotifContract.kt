/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerNotifContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.26
 */

package com.mtv.app.shopme.feature.seller.contract

import com.mtv.app.shopme.feature.seller.model.SellerNotifItem
import com.mtv.based.core.network.utils.ResourceFirebase
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.ERROR_STRING

data class SellerNotifState(
    val notificationState: ResourceFirebase<String> = ResourceFirebase.Loading,
    val activeDialog: SellerNotifDialog? = null
)

data class SellerNotifData(
    val localNotification: List<SellerNotifItem> = emptyList(),
)

data class SellerNotifEvent(
    val onNotificationClicked: (item: SellerNotifItem) -> Unit,
    val onGetNotification: () -> Unit,
    val onClearNotification: () -> Unit,
    val onDismissActiveDialog: () -> Unit,
)

data class SellerNotifNavigation(
    val onBack: () -> Unit,
)

sealed class SellerNotifDialog {
    data class Error(
        val message: String = ERROR_STRING
    ) : SellerNotifDialog()

    object Success : SellerNotifDialog()
}