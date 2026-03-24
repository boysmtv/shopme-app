/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.43
 */

package com.mtv.app.shopme.feature.customer.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.ProfileDataListener
import com.mtv.app.shopme.feature.customer.contract.ProfileEventListener
import com.mtv.app.shopme.feature.customer.contract.ProfileNavigationListener
import com.mtv.app.shopme.feature.customer.contract.ProfileStateListener
import com.mtv.app.shopme.feature.customer.presentation.ProfileViewModel
import com.mtv.app.shopme.feature.customer.ui.ProfileScreen
import com.mtv.app.shopme.nav.customer.CustomerDestinations
import com.mtv.app.shopme.nav.seller.SellerDestinations

@Composable
fun ProfileRoute(nav: NavController) {
    BaseRoute<ProfileViewModel, ProfileStateListener, ProfileDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            ProfileScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = profileEvent(vm, nav),
                uiNavigation = profileNavigation(nav)
            )
        }
    }
}

private fun profileEvent(vm: ProfileViewModel, nav: NavController) = ProfileEventListener(
    onDismissDialog = vm::dismissDialog,
    onCheckTncCafe = {
        val navigation = profileNavigation(nav)
        if (vm.doCheckTncCafe()) {
            navigation.onNavigateToTnc()
        } else {
            navigation.onNavigateToSeller()
        }
    }
)

private fun profileNavigation(nav: NavController) = ProfileNavigationListener(
    onEditProfile = { nav.navigate(CustomerDestinations.EDIT_PROFILE_GRAPH) },
    onOrderHistory = { nav.navigate(CustomerDestinations.ORDER_HISTORY_GRAPH) },
    onSettings = { nav.navigate(CustomerDestinations.SETTINGS_GRAPH) },
    onHelpCenter = { nav.navigate(CustomerDestinations.HELP_GRAPH) },
    onOrder = { nav.navigate(CustomerDestinations.ORDER_GRAPH) },
    onNavigateToTnc = { nav.navigate(SellerDestinations.SELLER_CREATE_TNC_GRAPH) },
    onNavigateToSeller = {
        nav.navigate(SellerDestinations.SELLER_GRAPH) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    },
)