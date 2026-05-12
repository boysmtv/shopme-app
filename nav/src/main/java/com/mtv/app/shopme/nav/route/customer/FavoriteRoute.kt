package com.mtv.app.shopme.nav.route.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.FavoriteEffect
import com.mtv.app.shopme.feature.customer.contract.FavoriteEvent
import com.mtv.app.shopme.feature.customer.presentation.FavoriteViewModel
import com.mtv.app.shopme.feature.customer.ui.FavoriteScreen
import com.mtv.app.shopme.nav.customer.CustomerNavActions

@Composable
fun FavoriteRoute(nav: NavController) {
    val vm: FavoriteViewModel = hiltViewModel()
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = FavoriteEvent.Load,
        onEffect = { effect ->
            when (effect) {
                FavoriteEffect.NavigateBack -> nav.popBackStack()
                is FavoriteEffect.NavigateToDetail -> CustomerNavActions.toDetail(nav, effect.foodId)
            }
        },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = { vm.onEvent(FavoriteEvent.DismissDialog) }
        ) {
            FavoriteScreen(
                state = uiState,
                event = vm::onEvent
            )
        }
    }
}
