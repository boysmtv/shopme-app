/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeTncRoute.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 13.10
 */

package com.mtv.app.shopme.nav.route.seller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.common.navbar.seller.SellerDestinations
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncEffect
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerCreateCafeTncViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerCreateCafeTncScreen

@Composable
fun SellerCreateCafeTncRoute(nav: NavController) {

    val vm: SellerCreateCafeTncViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerCreateCafeTncEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = {
                vm.onEvent(SellerCreateCafeTncEvent.DismissDialog)
            }
        ) {
            SellerCreateCafeTncScreen(
                state = uiState,
                event = vm::onEvent
            )
        }
    }
}

private fun handleEffect(
    nav: NavController,
    effect: SellerCreateCafeTncEffect
) {
    when (effect) {
        SellerCreateCafeTncEffect.NavigateBack -> nav.popBackStack()
        SellerCreateCafeTncEffect.NavigateNext -> {
            nav.navigate(SellerDestinations.SELLER_CREATE_GRAPH)
        }
    }
}