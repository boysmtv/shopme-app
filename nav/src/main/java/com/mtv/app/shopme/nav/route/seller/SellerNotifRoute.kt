/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerNotifRoute.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.27
 */

package com.mtv.app.shopme.nav.route.seller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerNotifEffect
import com.mtv.app.shopme.feature.seller.contract.SellerNotifEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerNotifViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerNotificationScreen

@Composable
fun SellerNotifRoute(nav: NavController) {

    val vm: SellerNotifViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerNotifEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = {
                vm.onEvent(SellerNotifEvent.DismissDialog)
            }
        ) {
            SellerNotificationScreen(
                state = uiState,
                event = vm::onEvent
            )
        }
    }
}

private fun handleEffect(
    nav: NavController,
    effect: SellerNotifEffect
) {
    when (effect) {
        SellerNotifEffect.NavigateBack -> nav.popBackStack()
    }
}