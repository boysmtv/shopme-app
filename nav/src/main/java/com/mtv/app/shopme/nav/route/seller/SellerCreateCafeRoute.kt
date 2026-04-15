/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeRoute.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 14.12
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
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeEffect
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerCreateCafeViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerCreateCafeScreen

@Composable
fun SellerCreateCafeRoute(nav: NavController) {

    val vm: SellerCreateCafeViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerCreateCafeEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = { vm.onEvent(SellerCreateCafeEvent.DismissDialog) }
        ) {
            SellerCreateCafeScreen(
                state = uiState,
                event = vm::onEvent
            )
        }
    }
}

private fun handleEffect(
    nav: NavController,
    effect: SellerCreateCafeEffect
) {
    when (effect) {
        SellerCreateCafeEffect.NavigateBack -> nav.popBackStack()

        SellerCreateCafeEffect.NavigateSuccess -> {
            nav.navigate(SellerDestinations.SELLER_GRAPH) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }

        SellerCreateCafeEffect.OpenImagePicker -> {

        }

        SellerCreateCafeEffect.OpenLocationPicker -> {
            // open maps
        }
    }
}