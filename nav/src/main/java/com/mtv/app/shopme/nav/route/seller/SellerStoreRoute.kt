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
import com.mtv.app.shopme.common.navbar.auth.AuthDestinations
import com.mtv.app.shopme.common.navbar.customer.CustomerDestinations
import com.mtv.app.shopme.common.navbar.seller.SellerDestinations
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
        SellerStoreEffect.NavigateBack ->
            nav.popBackStack()

        SellerStoreEffect.NavigateToOrders ->
            nav.navigate(SellerDestinations.ORDER)

        SellerStoreEffect.NavigateToEditProfile ->
            nav.navigate(CustomerDestinations.EDIT_PROFILE_GRAPH)

        SellerStoreEffect.NavigateToStoreSettings ->
            nav.navigate(SellerDestinations.SELLER_EDIT_STORE_GRAPH)

        SellerStoreEffect.NavigateToBankAccount ->
            nav.navigate(SellerDestinations.SELLER_PAYMENT_METHOD_GRAPH)

        SellerStoreEffect.NavigateToChangePassword ->
            nav.navigate(AuthDestinations.PASSWORD_GRAPH)

        SellerStoreEffect.NavigateToHelpCenter ->
            nav.navigate(CustomerDestinations.HELP_GRAPH)

        SellerStoreEffect.NavigateToCustomerHome ->
            nav.navigate(CustomerDestinations.HOME_GRAPH) {
                popUpTo(SellerDestinations.SELLER_GRAPH) { inclusive = true }
            }

        SellerStoreEffect.LogoutSuccess -> {
            nav.navigate(AuthDestinations.LOGIN_GRAPH) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}
