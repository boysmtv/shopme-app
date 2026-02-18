/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.feature.customer.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.OrderDataListener
import com.mtv.app.shopme.feature.customer.contract.OrderEventListener
import com.mtv.app.shopme.feature.customer.contract.OrderNavigationListener
import com.mtv.app.shopme.feature.customer.contract.OrderStateListener
import com.mtv.app.shopme.feature.customer.presentation.OrderViewModel
import com.mtv.app.shopme.feature.customer.ui.OrderScreen
import com.mtv.app.shopme.nav.CustomerDestinations
import com.mtv.app.shopme.nav.CustomerBottomNavItem

@Composable
fun OrderRoute(nav: NavController) {
    BaseRoute<OrderViewModel, OrderStateListener, OrderDataListener> { vm, base, uiState, uiData ->
        BaseScreen(
            baseUiState = base,
            onDismissError = vm::dismissError
        ) {
            OrderScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = orderEvent(vm),
                uiNavigation = orderNavigation(nav)
            )
        }
    }
}

private fun orderEvent(vm: OrderViewModel) = OrderEventListener(
    onReload = vm::loadOrders,
    onOrderClick = vm::selectOrder
)

private fun orderNavigation(nav: NavController) = OrderNavigationListener(
    onDetail = { orderId ->
        nav.navigate("${CustomerDestinations.ORDER_DETAIL_GRAPH}/$orderId")
    },
    onChatClick = {
        nav.navigate(CustomerBottomNavItem.Chat.route) {
            launchSingleTop = true
            restoreState = true

            popUpTo(nav.graph.startDestinationId) {
                saveState = true
            }
        }
    },
    onBack = { nav.popBackStack() },
)
