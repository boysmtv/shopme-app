/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderRoute.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 16.30
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
import com.mtv.app.shopme.feature.seller.contract.SellerOrderEffect
import com.mtv.app.shopme.feature.seller.contract.SellerOrderEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerOrderViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerOrderScreen

@Composable
fun SellerOrderRoute(nav: NavController) {

    val vm: SellerOrderViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerOrderEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = {
                vm.onEvent(SellerOrderEvent.DismissDialog)
            }
        ) {
            SellerOrderScreen(
                state = uiState,
                event = vm::onEvent
            )
        }
    }
}

private fun handleEffect(
    nav: NavController,
    effect: SellerOrderEffect
) {
    when (effect) {
        is SellerOrderEffect.NavigateToOrderDetail ->
            nav.navigate(SellerDestinations.navigateToOrderDetail(effect.orderId))
    }
}
