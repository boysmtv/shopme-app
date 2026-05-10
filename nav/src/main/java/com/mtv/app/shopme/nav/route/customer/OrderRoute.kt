/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.nav.route.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.common.navbar.customer.CustomerBottomNavItem
import com.mtv.app.shopme.common.navbar.customer.CustomerDestinations
import com.mtv.app.shopme.feature.customer.contract.OrderEffect
import com.mtv.app.shopme.feature.customer.contract.OrderEvent
import com.mtv.app.shopme.feature.customer.presentation.OrderViewModel
import com.mtv.app.shopme.feature.customer.ui.OrderScreen

@Composable
fun OrderRoute(nav: NavController) {

    val vm: OrderViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = OrderEvent.Load,
        onEffect = { handleOrderEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(OrderEvent.DismissDialog) }
            ) {
                OrderScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleOrderEffect(
    nav: NavController,
    effect: OrderEffect
) {
    when (effect) {
        OrderEffect.NavigateBack -> nav.popBackStack()

        OrderEffect.NavigateToChat -> {
            nav.navigate(CustomerBottomNavItem.Chat.route) {
                launchSingleTop = true
                restoreState = true

                popUpTo(nav.graph.startDestinationId) {
                    saveState = true
                }
            }
        }

        is OrderEffect.NavigateToDetail -> {
            nav.navigate(CustomerDestinations.navigateToOrderDetail(effect.orderId))
        }
    }
}
