/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class HomeStateListener(
    val emptyState: ResourceFirebase<Unit> = ResourceFirebase.Loading,
    val activeDialog: com.mtv.app.shopme.feature.customer.contract.HomeDialog? = null
)

data class HomeDataListener(
    val emptyData: String? = null
)

data class HomeEventListener(
    val onDismissActiveDialog: () -> Unit
)

data class HomeNavigationListener(
    val onNavigateToDetail: () -> Unit = {},
    val onNavigateToSearch: () -> Unit = {},
    val onNavigateToNotif: () -> Unit = {},
)

sealed class HomeDialog {
    object Success : HomeDialog()
}
