/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.nav.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.SearchEffect
import com.mtv.app.shopme.feature.customer.contract.SearchEvent
import com.mtv.app.shopme.feature.customer.presentation.SearchViewModel
import com.mtv.app.shopme.feature.customer.ui.SearchScreen
import com.mtv.app.shopme.nav.customer.CustomerNavActions

@Composable
fun SearchRoute(nav: NavController) {

    val vm: SearchViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SearchEvent.Load,
        onEffect = { handleSearchEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = { vm.onEvent(SearchEvent.BackClicked) }
        ) {
            SearchScreen(
                state = uiState,
                event = vm::onEvent
            )
        }
    }
}

private fun handleSearchEffect(
    nav: NavController,
    effect: SearchEffect
) {
    when (effect) {
        is SearchEffect.NavigateBack -> nav.popBackStack()
        is SearchEffect.NavigateToDetail -> {
            CustomerNavActions.toDetail(nav, effect.id)
        }
    }
}