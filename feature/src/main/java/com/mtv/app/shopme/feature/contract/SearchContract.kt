/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: SearchContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class SearchStateListener(
    val emptyState: ResourceFirebase<Unit> = ResourceFirebase.Loading,
    val activeDialog: SearchDialog? = null
)

data class SearchDataListener(
    val emptyData: String? = null
)

data class SearchEventListener(
    val onDismissActiveDialog: () -> Unit
)

data class SearchNavigationListener(
    val onBack: () -> Unit,
)

sealed class SearchDialog {
    object Success : SearchDialog()
}
