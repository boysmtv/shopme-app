/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerStoreRoute.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 10.01
 */

package com.mtv.app.shopme.nav.route.seller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerStoreEffect
import com.mtv.app.shopme.feature.seller.contract.SellerStoreEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerProfileViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerStoreScreen

@Composable
fun SellerStoreRoute(nav: NavController) {

    val vm: SellerProfileViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerStoreEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = {
                vm.onEvent(SellerStoreEvent.DismissDialog)
            }
        ) {
            SellerStoreScreen(
                state = uiState,
                event = vm::onEvent
            )
        }
    }
}

private fun handleEffect(
    nav: NavController,
    effect: SellerStoreEffect
) {
    when (effect) {

        SellerStoreEffect.NavigateToEditProfile ->
            nav.navigate("seller_edit_profile")

        SellerStoreEffect.NavigateToStoreSettings ->
            nav.navigate("seller_store_settings")

        SellerStoreEffect.NavigateToBankAccount ->
            nav.navigate("seller_bank_account")

        SellerStoreEffect.NavigateToChangePassword ->
            nav.navigate("seller_change_password")

        SellerStoreEffect.NavigateToHelpCenter ->
            nav.navigate("seller_help_center")

        SellerStoreEffect.LogoutSuccess -> {
            // biasanya ke login screen
            nav.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}