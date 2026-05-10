/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderHistoryRoute.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 09.58
 */

package com.mtv.app.shopme.nav.route.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.common.navbar.customer.CustomerDestinations
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryEffect
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryEvent
import com.mtv.app.shopme.feature.customer.presentation.OrderHistoryViewModel
import com.mtv.app.shopme.feature.customer.ui.OrderHistoryScreen

@Composable
fun OrderHistoryRoute(nav: NavController) {

    val vm: OrderHistoryViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = OrderHistoryEvent.Load,
        onEffect = { handleOrderHistoryEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(OrderHistoryEvent.DismissDialog) }
            ) {
                OrderHistoryScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleOrderHistoryEffect(
    nav: NavController,
    effect: OrderHistoryEffect
) {
    when (effect) {
        OrderHistoryEffect.NavigateBack -> nav.popBackStack()
        is OrderHistoryEffect.NavigateToDetail -> {
            nav.navigate(CustomerDestinations.navigateToOrderDetail(effect.item.id))
        }
    }
}
