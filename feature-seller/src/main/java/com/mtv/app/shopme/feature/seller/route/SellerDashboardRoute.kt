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
import com.mtv.app.shopme.feature.seller.contract.SellerDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerStateListener
import com.mtv.app.shopme.feature.seller.presentation.SellerDashboardViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerDashboardScreen
import com.mtv.app.shopme.nav.SellerDestinations

@Composable
fun SellerDashboardRoute(nav: NavController) {
    BaseRoute<SellerDashboardViewModel, SellerStateListener, SellerDataListener> { vm, base, uiState, uiData ->
        BaseScreen(
            baseUiState = base,
            onDismissError = vm::dismissError
        ) {
            SellerDashboardScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = sellerEvent(vm),
                uiNavigation = sellerNavigation(nav)
            )
        }
    }
}

private fun sellerEvent(vm: SellerDashboardViewModel) = SellerEventListener(
    onRefresh = {

    }
)

private fun sellerNavigation(nav: NavController) = SellerNavigationListener(
    onNavigateToProduct = {
        nav.navigate(SellerDestinations.PRODUCT)
    },
    onNavigateToOrder = {
        nav.navigate(SellerDestinations.ORDER)
    },
    onNavigateToOrderDetail = { orderId ->
        nav.navigate("seller_order_detail/$orderId")
    }
)

