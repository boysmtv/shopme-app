/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: MapContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class MapStateListener(
    val emptyState: ResourceFirebase<Unit> = ResourceFirebase.Loading,
    val activeDialog: MapDialog? = null
)

data class MapDataListener(
    val emptyData: String? = null
)

data class MapEventListener(
    val onDismissActiveDialog: () -> Unit
)

data class MapNavigationListener(
    val onBack: () -> Unit
)

sealed class MapDialog {
    object Success : MapDialog()
}
