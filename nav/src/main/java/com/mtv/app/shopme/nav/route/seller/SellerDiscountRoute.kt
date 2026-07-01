package com.mtv.app.shopme.nav.route.seller

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.navbar.seller.SellerDestinations
import com.mtv.app.shopme.feature.seller.contract.SellerDiscountEffect
import com.mtv.app.shopme.feature.seller.contract.SellerDiscountEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerDiscountViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerDiscountScreen

@Composable
fun SellerDiscountRoute(nav: NavController) {
    val vm: SellerDiscountViewModel = hiltViewModel()
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerDiscountEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        SellerDiscountScreen(
            state = uiState,
            event = vm::onEvent
        )
    }
}

private fun handleEffect(
    nav: NavController,
    effect: SellerDiscountEffect
) {
    when (effect) {
        SellerDiscountEffect.NavigateBack ->
            nav.popBackStack()
    }
}
