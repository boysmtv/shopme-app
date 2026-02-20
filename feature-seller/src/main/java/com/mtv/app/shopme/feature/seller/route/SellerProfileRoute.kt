/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProfileRoute.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 10.01
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerProfileDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerProfileEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerProfileNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerProfileStateListener
import com.mtv.app.shopme.feature.seller.presentation.SellerProfileViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerProfileScreen

@Composable
fun SellerProfileRoute(nav: NavController) {
    BaseRoute<SellerProfileViewModel, SellerProfileStateListener, SellerProfileDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            SellerProfileScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = sellerProfileEvent(vm),
                uiNavigation = sellerProfileNavigation(nav)
            )
        }
    }
}

private fun sellerProfileEvent(vm: SellerProfileViewModel) =
    SellerProfileEventListener(
        onToggleOnline = vm::toggleOnline,
        onEditProfile = {},
        onLogout = vm::logout
    )

private fun sellerProfileNavigation(nav: NavController) =
    SellerProfileNavigationListener(
        navigateToEditProfile = { nav.navigate("seller_edit_profile") },
        navigateToStoreSettings = { nav.navigate("seller_store_settings") },
        navigateToBankAccount = { nav.navigate("seller_bank_account") },
        navigateToChangePassword = { nav.navigate("seller_change_password") },
        navigateToHelpCenter = { nav.navigate("seller_help_center") }
    )
