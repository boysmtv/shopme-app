/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.43
 */

/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.43
 */

package com.mtv.app.shopme.nav.route.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
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

    val refreshTick by nav.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("refreshTick", 0L)
        ?.collectAsStateWithLifecycle()
        ?: remember { mutableLongStateOf(0L) }

    LaunchedEffect(refreshTick) {
        if (refreshTick > 0L) {
            vm.onEvent(CartEvent.Load)
        }
    }

    BaseRoute(
        viewModel = vm,
        onLoad = CartEvent.Load,
        onEffect = null,
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = {
                vm.onEvent(CartEvent.DismissDialog)
            }
        ) {
            CartScreen(
                state = uiState,
                effectFlow = vm.effect,
                event = vm::onEvent,
                onNavigateToDetail = { foodId ->
                    CustomerNavActions.toDetail(nav, foodId)
                },
                onNavigateToOrder = {
                    CustomerNavActions.toOrder(nav)
                },
                onNavigateToEditProfile = {
                    CustomerNavActions.toEditProfile(nav)
                }
            )
        }
    }
}

/**
 * Kept intentionally for backward compatibility with older route wiring.
 *
 * CartEffect is now handled inside CartScreen because Cart contains local UI effects:
 * - OpenPinSheet
 * - ShowSuccessDialog
 * - ShowSnackbar
 *
 * CartRoute should only provide navigation callbacks.
 */
@Suppress("unused")
private fun handleCartEffect(
    nav: NavController,
    effect: CartEffect
) {
    when (effect) {
        CartEffect.OpenPinSheet -> Unit

        CartEffect.ShowSuccessDialog -> Unit

        is CartEffect.ShowSnackbar -> Unit

        CartEffect.NavigateToOrder -> {
            CustomerNavActions.toOrder(nav)
        }

        CartEffect.NavigateToEditProfile -> {
            CustomerNavActions.toEditProfile(nav)
        }
    }
}