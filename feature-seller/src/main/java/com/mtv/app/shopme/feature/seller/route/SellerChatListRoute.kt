/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatListRoute.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.31
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerChatListDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerChatListEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerChatListNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerChatListStateListener
import com.mtv.app.shopme.feature.seller.presentation.SellerChatListViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerChatListScreen
import com.mtv.app.shopme.nav.SellerDestinations

@Composable
fun SellerChatListRoute(nav: NavController) {
    BaseRoute<SellerChatListViewModel, SellerChatListStateListener, SellerChatListDataListener> { vm, base, uiState, uiData ->
        BaseScreen(
            baseUiState = base,
            onDismissError = vm::dismissError
        ) {
            SellerChatListScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = sellerChatListEvent(vm),
                uiNavigation = sellerChatListNavigation(nav)
            )
        }
    }
}

private fun sellerChatListEvent(vm: SellerChatListViewModel) = SellerChatListEventListener(
    onDeleteChat = {
    }
)

private fun sellerChatListNavigation(nav: NavController) = SellerChatListNavigationListener(
    onBack = { nav.popBackStack() },
    navigateToChat = {
        nav.navigate(SellerDestinations.SELLER_CHAT_DETAIL_GRAPH)
    }
)
