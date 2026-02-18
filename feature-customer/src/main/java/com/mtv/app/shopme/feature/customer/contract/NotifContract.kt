/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotifContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class NotifStateListener(
    val emptyState: ResourceFirebase<Unit> = ResourceFirebase.Loading,
    val activeDialog: com.mtv.app.shopme.feature.customer.contract.NotifDialog? = null
)

data class NotifDataListener(
    val emptyData: String? = null
)

data class NotifEventListener(
    val onDismissActiveDialog: () -> Unit
)

data class NotifNavigationListener(
    val onBack: () -> Unit
)

sealed class NotifDialog {
    object Success : NotifDialog()
}
