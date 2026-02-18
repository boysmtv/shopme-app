/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.feature.customer.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.DetailDataListener
import com.mtv.app.shopme.feature.customer.contract.DetailEventListener
import com.mtv.app.shopme.feature.customer.contract.DetailNavigationListener
import com.mtv.app.shopme.feature.customer.contract.DetailStateListener
import com.mtv.app.shopme.feature.customer.presentation.DetailViewModel
import com.mtv.app.shopme.feature.customer.ui.DetailScreen
import com.mtv.app.shopme.nav.CustomerDestinations
import com.mtv.app.shopme.nav.CustomerBottomNavItem

@Composable
fun DetailRoute(nav: NavController) {
    BaseRoute<DetailViewModel, DetailStateListener, DetailDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            DetailScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = detailEvent(vm),
                uiNavigation = detailNavigation(nav)
            )
        }
    }
}

private fun detailEvent(vm: DetailViewModel) = DetailEventListener(
    onDismissActiveDialog = {}
)

private fun detailNavigation(nav: NavController) = DetailNavigationListener(
    onBack = {},
    onChatClick = {
        nav.navigate(CustomerDestinations.CHAT_GRAPH)
    },
    onAddToCart = {
        nav.navigate(CustomerBottomNavItem.Cart.route) {
            launchSingleTop = true
            restoreState = true

            popUpTo(nav.graph.startDestinationId) {
                saveState = true
            }
        }
    },
    onClickCafe = { nav.navigate(CustomerDestinations.CAFE_GRAPH) }
)
