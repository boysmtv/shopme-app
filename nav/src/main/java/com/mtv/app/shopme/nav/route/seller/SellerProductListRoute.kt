/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductListRoute.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.58
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
import com.mtv.app.shopme.feature.seller.contract.SellerProductListEffect
import com.mtv.app.shopme.feature.seller.contract.SellerProductListEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerProductListViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerProductListScreen

@Composable
fun SellerProductListRoute(nav: NavController) {

    val vm: SellerProductListViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerProductListEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = {
                vm.onEvent(SellerProductListEvent.DismissDialog)
            }
        ) {
            SellerProductListScreen(
                state = uiState,
                event = vm::onEvent
            )
        }
    }
}

private fun handleEffect(
    nav: NavController,
    effect: SellerProductListEffect
) {
    when (effect) {

        SellerProductListEffect.NavigateBack ->
            nav.popBackStack()

        SellerProductListEffect.NavigateToAdd ->
            nav.navigate(SellerDestinations.SELLER_PRODUCT_ADD_GRAPH)

        is SellerProductListEffect.NavigateToEdit ->
            nav.navigate(SellerDestinations.navigateToProductEdit(effect.productId))
    }
}
