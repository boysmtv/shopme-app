/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderHistoryRoute.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 09.58
 */

package com.mtv.app.shopme.feature.customer.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryDataListener
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryEventListener
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryNavigationListener
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryStateListener
import com.mtv.app.shopme.feature.customer.presentation.OrderHistoryViewModel
import com.mtv.app.shopme.feature.customer.ui.OrderHistoryScreen

@Composable
fun OrderHistoryRoute(nav: NavController) {
    BaseRoute<OrderHistoryViewModel, OrderHistoryStateListener, OrderHistoryDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            OrderHistoryScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = OrderHistoryEventListener(
                    onRefresh = vm::loadOrders,
                    onClickOrder = {}
                ),
                uiNavigation = OrderHistoryNavigationListener(
                    onBack = { nav.popBackStack() }
                )
            )
        }
    }
}