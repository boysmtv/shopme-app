/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerDashboardRoute.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.15
 */

package com.mtv.app.shopme.nav.route.seller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.common.navbar.seller.SellerDestinations
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEffect
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerDashboardViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerDashboardScreen

@Composable
fun SellerDashboardRoute(nav: NavController) {

    val vm: SellerDashboardViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerDashboardEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = {
                vm.onEvent(SellerDashboardEvent.DismissDialog)
            }
        ) {
            SellerDashboardScreen(
                state = uiState,
                event = vm::onEvent
            )
        }
    }
}

private fun handleEffect(
    nav: NavController,
    effect: SellerDashboardEffect
) {
    when (effect) {
        SellerDashboardEffect.NavigateToProduct ->
            nav.navigate(SellerDestinations.PRODUCT)
        SellerDashboardEffect.NavigateToOrder ->
            nav.navigate(SellerDestinations.ORDER)
        is SellerDashboardEffect.NavigateToOrderDetail ->
            nav.navigate(SellerDestinations.navigateToOrderDetail(effect.orderId))
        SellerDashboardEffect.NavigateToNotif ->
            nav.navigate(SellerDestinations.SELLER_NOTIFICATION_GRAPH)
    }
}
