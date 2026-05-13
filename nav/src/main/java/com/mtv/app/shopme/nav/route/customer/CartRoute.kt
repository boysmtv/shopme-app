/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.43
 */

package com.mtv.app.shopme.nav.route.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.CartEffect
import com.mtv.app.shopme.feature.customer.contract.CartEvent
import com.mtv.app.shopme.feature.customer.presentation.CartViewModel
import com.mtv.app.shopme.feature.customer.ui.CartScreen
import com.mtv.app.shopme.nav.customer.CustomerNavActions

@Composable
fun CartRoute(nav: NavController) {

    val vm: CartViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = CartEvent.Load,
        onEffect = { handleCartEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = { vm.onEvent(CartEvent.DismissDialog) }
        ) {
            CartScreen(
                state = uiState,
                event = vm::onEvent,
                onNavigateToDetail = { foodId ->
                    CustomerNavActions.toDetail(nav, foodId)
                }
            )
        }
    }
}

private fun handleCartEffect(
    nav: NavController,
    effect: CartEffect
) {
    when (effect) {
        CartEffect.OpenPinSheet -> { }
        CartEffect.NavigateToOrder -> CustomerNavActions.toOrder(nav)
        CartEffect.NavigateToEditProfile -> CustomerNavActions.toEditProfile(nav)
        CartEffect.ShowSuccessDialog -> CustomerNavActions.toOrder(nav)
    }
}
