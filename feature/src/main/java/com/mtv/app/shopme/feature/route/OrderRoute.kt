/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.feature.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.contract.OrderDataListener
import com.mtv.app.shopme.feature.contract.OrderEventListener
import com.mtv.app.shopme.feature.contract.OrderNavigationListener
import com.mtv.app.shopme.feature.contract.OrderStateListener
import com.mtv.app.shopme.feature.presentation.OrderViewModel
import com.mtv.app.shopme.feature.ui.OrderScreen
import com.mtv.app.shopme.nav.AppDestinations
import com.mtv.app.shopme.nav.BottomNavItem

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
        nav.navigate("${AppDestinations.ORDER_DETAIL_GRAPH}/$orderId")
    },
    onChatClick = {
        nav.navigate(BottomNavItem.Chat.route) {
            launchSingleTop = true
            restoreState = true

            popUpTo(nav.graph.startDestinationId) {
                saveState = true
            }
        }
    },
    onBack = { nav.popBackStack() },
)
