package com.mtv.app.shopme.nav.route.seller

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.feature.seller.contract.SellerReviewEffect
import com.mtv.app.shopme.feature.seller.contract.SellerReviewEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerReviewViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerReviewScreen

@Composable
fun SellerReviewRoute(nav: NavController) {
    val vm: SellerReviewViewModel = hiltViewModel()
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerReviewEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        SellerReviewScreen(
            state = uiState,
            event = vm::onEvent
        )
    }
}

private fun handleEffect(
    nav: NavController,
    effect: SellerReviewEffect
) {
    when (effect) {
        SellerReviewEffect.NavigateBack ->
            nav.popBackStack()
    }
}
