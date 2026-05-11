/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeRoute.kt
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
import com.mtv.app.shopme.feature.customer.contract.DetailEffect
import com.mtv.app.shopme.feature.customer.contract.DetailEvent
import com.mtv.app.shopme.feature.customer.presentation.DetailViewModel
import com.mtv.app.shopme.feature.customer.ui.DetailScreen
import com.mtv.app.shopme.nav.customer.CustomerNavActions

@Composable
fun DetailRoute(nav: NavController) {

    val vm: DetailViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = DetailEvent.Load,
        onEffect = { handleDetailEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = { vm.onEvent(DetailEvent.BackClicked) }
        ) {
            DetailScreen(
                state = uiState,
                event = vm::onEvent
            )
        }
    }
}

private fun handleDetailEffect(nav: NavController, effect: DetailEffect) {
    when (effect) {
        is DetailEffect.NavigateBack -> nav.popBackStack()
        is DetailEffect.NavigateToChat -> CustomerNavActions.toChat(nav, effect.chatId)
        is DetailEffect.NavigateToCart -> CustomerNavActions.toCart(nav)
        is DetailEffect.NavigateToCafe -> CustomerNavActions.toCafe(nav, effect.cafeId)
        is DetailEffect.NavigateToDetail -> CustomerNavActions.toDetail(nav, effect.foodId)
    }
}
