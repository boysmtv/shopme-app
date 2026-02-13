/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.43
 */

package com.mtv.app.shopme.feature.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.contract.ProfileDataListener
import com.mtv.app.shopme.feature.contract.ProfileEventListener
import com.mtv.app.shopme.feature.contract.ProfileNavigationListener
import com.mtv.app.shopme.feature.contract.ProfileStateListener
import com.mtv.app.shopme.feature.presentation.ProfileViewModel
import com.mtv.app.shopme.feature.ui.ProfileScreen
import com.mtv.app.shopme.nav.AppDestinations

@Composable
fun ProfileRoute(nav: NavController) {
    BaseRoute<ProfileViewModel, ProfileStateListener, ProfileDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            ProfileScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = profileEvent(vm),
                uiNavigation = profileNavigation(nav)
            )
        }
    }
}

private fun profileEvent(vm: ProfileViewModel) = ProfileEventListener(
    onDismissDialog = vm::dismissDialog
)

private fun profileNavigation(nav: NavController) = ProfileNavigationListener(
    onEditProfile = { nav.navigate(AppDestinations.EDIT_PROFILE_GRAPH) },
    onAddress = { nav.navigate(AppDestinations.EDIT_ADDRESS_GRAPH) },
    onPayment = { nav.navigate("") },
    onOrderHistory = { nav.navigate("") },
    onTracking = { nav.navigate("") },
    onWishlist = { nav.navigate("") },
    onSettings = { nav.navigate("") },
    onHelpCenter = { nav.navigate("") },
    onAbout = { nav.navigate("") },
    onOrder = { nav.navigate(AppDestinations.ORDER_GRAPH) },
)