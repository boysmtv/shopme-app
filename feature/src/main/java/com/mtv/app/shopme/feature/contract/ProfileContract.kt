/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: ProfileContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.contract

data class ProfileStateListener(
    val activeDialog: ProfileDialog? = null
)

data class ProfileDataListener(
    val userName: String = "Dedy Wijaya",
    val email: String = "dedy.wijaya@email.com"
)

data class ProfileEventListener(
    val onDismissDialog: () -> Unit = {},
)

data class ProfileNavigationListener(
    val onEditProfile: () -> Unit = {},
    val onAddress: () -> Unit = {},
    val onPayment: () -> Unit = {},
    val onOrderHistory: () -> Unit = {},
    val onTracking: () -> Unit = {},
    val onWishlist: () -> Unit = {},
    val onSettings: () -> Unit = {},
    val onHelpCenter: () -> Unit = {},
    val onAbout: () -> Unit = {},
    val onBack: () -> Unit = {},
    val onOrder: () -> Unit = {}
)

sealed class ProfileDialog {
    object LogoutConfirm : ProfileDialog()
}
