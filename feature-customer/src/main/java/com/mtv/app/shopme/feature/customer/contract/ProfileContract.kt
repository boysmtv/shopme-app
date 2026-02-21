/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import androidx.navigation.NavController

data class ProfileStateListener(
    val activeDialog: com.mtv.app.shopme.feature.customer.contract.ProfileDialog? = null
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
    val onOrderHistory: () -> Unit = {},
    val onSettings: () -> Unit = {},
    val onHelpCenter: () -> Unit = {},
    val onOrder: () -> Unit = {},
    val onNavigateToSeller: (navController: NavController) -> Unit = {}
)

sealed class ProfileDialog {
    object LogoutConfirm : ProfileDialog()
}
