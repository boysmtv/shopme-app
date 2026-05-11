package com.mtv.app.shopme.nav.route.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.OrderDetailEffect
import com.mtv.app.shopme.feature.customer.contract.OrderDetailEvent
import com.mtv.app.shopme.feature.customer.presentation.OrderDetailViewModel
import com.mtv.app.shopme.feature.customer.ui.OrderDetailScreen
import com.mtv.app.shopme.nav.customer.CustomerNavActions

@Composable
fun OrderDetailRoute(nav: NavController) {
    val vm: OrderDetailViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = OrderDetailEvent.Load,
        onEffect = { handleOrderDetailEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = { vm.onEvent(OrderDetailEvent.DismissDialog) }
        ) {
            OrderDetailScreen(
                state = uiState,
                event = vm::onEvent
            )
        }
    }
}

private fun handleOrderDetailEffect(
    nav: NavController,
    effect: OrderDetailEffect
) {
    when (effect) {
        OrderDetailEffect.NavigateBack -> nav.popBackStack()
        is OrderDetailEffect.NavigateToChat -> CustomerNavActions.toChat(nav, effect.chatId)
    }
}
