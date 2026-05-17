/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.nav.route.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.HomeEffect
import com.mtv.app.shopme.feature.customer.contract.HomeEvent
import com.mtv.app.shopme.feature.customer.presentation.HomeViewModel
import com.mtv.app.shopme.feature.customer.ui.HomeScreen
import com.mtv.app.shopme.nav.customer.CustomerNavActions

@Composable
fun HomeRoute(nav: NavController) {

    val vm: HomeViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()
    val refreshTick by nav.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("refreshTick", 0L)
        ?.collectAsStateWithLifecycle()
        ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableLongStateOf(0L) }

    LaunchedEffect(refreshTick) {
        if (refreshTick > 0L) vm.onEvent(HomeEvent.Load)
    }

    BaseRoute(
        viewModel = vm,
        onLoad = HomeEvent.Load,
        onEffect = { handleHomeEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(HomeEvent.DismissDialog) }
            ) {
                HomeScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )

}

private fun handleHomeEffect(
    nav: NavController,
    effect: HomeEffect
) {
    when (effect) {
        is HomeEffect.NavigateToDetail -> CustomerNavActions.toDetail(nav, effect.id)
        is HomeEffect.NavigateToSearch -> CustomerNavActions.toSearch(nav, effect.query)
        is HomeEffect.NavigateToNotif -> CustomerNavActions.toNotif(nav)
    }
}
