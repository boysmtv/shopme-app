/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderRoute.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 16.30
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderStateListener
import com.mtv.app.shopme.feature.seller.presentation.SellerOrderViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerOrderScreen
import com.mtv.app.shopme.nav.SellerDestinations

@Composable
fun SellerOrderRoute(nav: NavController) {
    BaseRoute<SellerOrderViewModel, SellerOrderStateListener, SellerOrderDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            SellerOrderScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = sellerOrderEvent(vm),
                uiNavigation = sellerOrderNavigation(nav)
            )
        }
    }
}

private fun sellerOrderEvent(vm: SellerOrderViewModel) = SellerOrderEventListener(
    onSelectFilter = {},
    onToggleOnline = {}
)

private fun sellerOrderNavigation(nav: NavController) = SellerOrderNavigationListener(
    onNavigateToOrderDetail = {
        nav.navigate(SellerDestinations.SELLER_ORDER_DETAIL_GRAPH)
    }
)
