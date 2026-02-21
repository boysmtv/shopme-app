/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatDetailRoute.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.32
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailStateListener
import com.mtv.app.shopme.feature.seller.presentation.SellerChatDetailViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerChatScreen

@Composable
fun SellerChatDetailRoute(nav: NavController) {
    BaseRoute<SellerChatDetailViewModel, SellerChatDetailStateListener, SellerChatDetailDataListener> { vm, base, uiState, uiData ->
        BaseScreen(
            baseUiState = base,
            onDismissError = vm::dismissError
        ) {
            SellerChatScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = sellerChatEvent(vm),
                uiNavigation = sellerChatNavigation(nav)
            )
        }
    }
}

private fun sellerChatEvent(vm: SellerChatDetailViewModel) = SellerChatDetailEventListener(
    onSendMessage = { message ->
        vm.sendMessage(message)
    }
)

private fun sellerChatNavigation(nav: NavController) = SellerChatDetailNavigationListener(
    onBack = { nav.popBackStack() }
)
