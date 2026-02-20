/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerDashboardRoute.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.15
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardStateListener
import com.mtv.app.shopme.feature.seller.presentation.SellerDashboardViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerDashboardScreen
import com.mtv.app.shopme.nav.SellerDestinations

@Composable
fun SellerDashboardRoute(nav: NavController) {
    BaseRoute<SellerDashboardViewModel, SellerDashboardStateListener, SellerDashboardDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            SellerDashboardScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = sellerDashboardEvent(vm),
                uiNavigation = sellerDashboardNavigation(nav)
            )
        }
    }
}

private fun sellerDashboardEvent(vm: SellerDashboardViewModel) = SellerDashboardEventListener(
    onRefresh = {

    }
)

private fun sellerDashboardNavigation(nav: NavController) = SellerDashboardNavigationListener(
    onNavigateToProduct = {
        nav.navigate(SellerDestinations.PRODUCT)
    },
    onNavigateToOrder = {
        nav.navigate(SellerDestinations.ORDER)
    },
    onNavigateToOrderDetail = { orderId ->
        nav.navigate("seller_order_detail/$orderId")
    },
    onNavigateToNotif = {
        nav.navigate(SellerDestinations.SELLER_NOTIFICATION_GRAPH)
    }
)

