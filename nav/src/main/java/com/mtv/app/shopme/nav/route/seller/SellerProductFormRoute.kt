/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductFormRoute.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 13.34
 */

package com.mtv.app.shopme.nav.route.seller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerProductFormEffect
import com.mtv.app.shopme.feature.seller.contract.SellerProductFormEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerProductFormViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerProductFormScreen

@Composable
fun SellerProductFormRoute(nav: NavController) {

    val vm: SellerProductFormViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerProductFormEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = {
                vm.onEvent(SellerProductFormEvent.DismissDialog)
            }
        ) {
            SellerProductFormScreen(
                state = uiState,
                event = vm::onEvent
            )
        }
    }
}

private fun handleEffect(
    nav: NavController,
    effect: SellerProductFormEffect
) {
    when (effect) {
        SellerProductFormEffect.NavigateBack ->
            nav.popBackStack()

        SellerProductFormEffect.SaveSuccess ->
            nav.popBackStack()

        SellerProductFormEffect.DeleteSuccess ->
            nav.popBackStack()
    }
}