/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerPaymentMethodRoute.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 22.20
 */

package com.mtv.app.shopme.nav.route.seller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodEffect
import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerPaymentMethodViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerPaymentMethodScreen

@Composable
fun SellerPaymentMethodRoute(nav: NavController) {

    val vm: SellerPaymentMethodViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerPaymentMethodEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = {
                vm.onEvent(SellerPaymentMethodEvent.DismissDialog)
            }
        ) {
            SellerPaymentMethodScreen(
                state = uiState,
                event = vm::onEvent
            )
        }
    }
}

private fun handleEffect(
    nav: NavController,
    effect: SellerPaymentMethodEffect
) {
    when (effect) {
        SellerPaymentMethodEffect.NavigateBack ->
            nav.popBackStack()

        SellerPaymentMethodEffect.SaveSuccess ->
            nav.popBackStack()
    }
}