package com.mtv.app.shopme.nav.route.seller

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.feature.seller.contract.SellerCategoryEffect
import com.mtv.app.shopme.feature.seller.contract.SellerCategoryEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerCategoryViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerCategoryScreen

@Composable
fun SellerCategoryRoute(nav: NavController) {
    val vm: SellerCategoryViewModel = hiltViewModel()
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerCategoryEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        SellerCategoryScreen(
            state = uiState,
            event = vm::onEvent
        )
    }
}

private fun handleEffect(
    nav: NavController,
    effect: SellerCategoryEffect
) {
    when (effect) {
        SellerCategoryEffect.NavigateBack ->
            nav.popBackStack()
    }
}
