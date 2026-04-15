/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatListRoute.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.31
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
import com.mtv.app.shopme.feature.seller.contract.SellerChatListEffect
import com.mtv.app.shopme.feature.seller.contract.SellerChatListEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerChatListViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerChatListScreen

@Composable
fun SellerChatListRoute(nav: NavController) {

    val vm: SellerChatListViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SellerChatListEvent.Load,
        onEffect = { handleSellerChatListEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(SellerChatListEvent.DismissDialog) }
            ) {
                SellerChatListScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleSellerChatListEffect(
    nav: NavController,
    effect: SellerChatListEffect
) {
    when (effect) {
        SellerChatListEffect.NavigateBack -> nav.popBackStack()

        is SellerChatListEffect.NavigateToChat -> {
            nav.navigate(SellerDestinations.SELLER_CHAT_DETAIL_GRAPH)
            // next improvement:
            // nav.navigate("seller_chat_detail/${effect.chatId}")
        }
    }
}