/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderDetailRoute.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 13.04
 */

package com.mtv.app.shopme.nav.route.seller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailEffect
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerOrderDetailViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerOrderDetailScreen

@Composable
fun SellerOrderDetailRoute(nav: NavController) {

    val vm: SellerOrderDetailViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerOrderDetailEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = {
                vm.onEvent(SellerOrderDetailEvent.DismissDialog)
            }
        ) {
            SellerOrderDetailScreen(
                state = uiState,
                event = vm::onEvent
            )
        }
    }
}

private fun handleEffect(
    nav: NavController,
    effect: SellerOrderDetailEffect
) {
    when (effect) {
        SellerOrderDetailEffect.NavigateBack ->
            nav.popBackStack()

        SellerOrderDetailEffect.UpdateSuccess ->
            nav.popBackStack()
    }
}