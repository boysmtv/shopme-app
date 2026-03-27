/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerStoreRoute.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 10.01
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerStoreDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerStoreEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerStoreNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerStoreStateListener
import com.mtv.app.shopme.feature.seller.presentation.SellerProfileViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerStoreScreen

@Composable
fun SellerStoreRoute(nav: NavController) {
    BaseRoute<SellerProfileViewModel, SellerStoreStateListener, SellerStoreDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, dismissDialog = vm::dismissError) {
            SellerStoreScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = sellerProfileEvent(vm),
                uiNavigation = sellerProfileNavigation(nav)
            )
        }
    }
}

private fun sellerProfileEvent(vm: SellerProfileViewModel) =
    SellerStoreEventListener(
        onToggleOnline = vm::toggleOnline,
        onEditStore = {},
        onLogout = vm::logout
    )

private fun sellerProfileNavigation(nav: NavController) =
    SellerStoreNavigationListener(
        navigateToEditProfile = { nav.navigate("seller_edit_profile") },
        navigateToStoreSettings = { nav.navigate("seller_store_settings") },
        navigateToBankAccount = { nav.navigate("seller_bank_account") },
        navigateToChangePassword = { nav.navigate("seller_change_password") },
        navigateToHelpCenter = { nav.navigate("seller_help_center") }
    )
