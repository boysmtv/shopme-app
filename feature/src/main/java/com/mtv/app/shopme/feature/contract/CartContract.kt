/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: CartContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class CartStateListener(
    val emptyState: ResourceFirebase<Unit> = ResourceFirebase.Loading,
    val activeDialog: CartDialog? = null
)

data class CartDataListener(
    val emptyData: String? = null
)

data class CartEventListener(
    val onDismissActiveDialog: () -> Unit
)

data class CartNavigationListener(
    val onBack: () -> Unit,
)

sealed class CartDialog {
    object Success : CartDialog()
}
