/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatDetailRoute.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.32
 */

package com.mtv.app.shopme.nav.route.seller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailEffect
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerChatDetailViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerChatScreen

@Composable
fun SellerChatDetailRoute(nav: NavController) {

    val vm: SellerChatDetailViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerChatDetailEvent.Load,
        onEffect = { handleSellerChatEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(SellerChatDetailEvent.DismissDialog) }
            ) {
                SellerChatScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleSellerChatEffect(
    nav: NavController,
    effect: SellerChatDetailEffect
) {
    when (effect) {
        SellerChatDetailEffect.NavigateBack -> nav.popBackStack()
    }
}