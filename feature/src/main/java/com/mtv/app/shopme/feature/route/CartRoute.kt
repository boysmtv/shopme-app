/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: CartRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.43
 */

package com.mtv.app.shopme.feature.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.contract.CartDataListener
import com.mtv.app.shopme.feature.contract.CartEventListener
import com.mtv.app.shopme.feature.contract.CartNavigationListener
import com.mtv.app.shopme.feature.contract.CartStateListener
import com.mtv.app.shopme.feature.presentation.CartViewModel
import com.mtv.app.shopme.feature.ui.CartScreen

@Composable
fun CartRoute(nav: NavController) {
    BaseRoute<CartViewModel, CartStateListener, CartDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            CartScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = cartEvent(vm),
                uiNavigation = cartNavigation(nav)
            )
        }
    }
}

private fun cartEvent(vm: CartViewModel) = CartEventListener(
    onDismissActiveDialog = {}
)

private fun cartNavigation(nav: NavController) = CartNavigationListener(
    onBack = { nav.popBackStack() },
)
