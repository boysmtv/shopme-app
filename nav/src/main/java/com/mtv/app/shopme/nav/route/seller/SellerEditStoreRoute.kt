/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerEditStoreRoute.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 15.34
 */

package com.mtv.app.shopme.nav.route.seller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreEffect
import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerEditStoreViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerEditStoreScreen

@Composable
fun SellerEditStoreRoute(nav: NavController) {

    val vm: SellerEditStoreViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerEditStoreEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = {
                vm.onEvent(SellerEditStoreEvent.DismissDialog)
            }
        ) {
            SellerEditStoreScreen(
                state = uiState,
                event = vm::onEvent
            )
        }
    }
}

private fun handleEffect(
    nav: NavController,
    effect: SellerEditStoreEffect
) {
    when (effect) {
        SellerEditStoreEffect.NavigateBack -> nav.popBackStack()

        SellerEditStoreEffect.SaveSuccess -> nav.popBackStack()

        SellerEditStoreEffect.OpenImagePicker -> {
            // trigger picker
        }
    }
}