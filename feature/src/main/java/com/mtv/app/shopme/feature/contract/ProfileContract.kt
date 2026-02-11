/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: ProfileContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class ProfileStateListener(
    val emptyState: ResourceFirebase<Unit> = ResourceFirebase.Loading,
    val activeDialog: ProfileDialog? = null
)

data class ProfileDataListener(
    val emptyData: String? = null
)

data class ProfileEventListener(
    val onDismissActiveDialog: () -> Unit
)

data class ProfileNavigationListener(
    val onBack: () -> Unit,
)

sealed class ProfileDialog {
    object Success : ProfileDialog()
}
