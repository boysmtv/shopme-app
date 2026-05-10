/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ListChatRoute.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 13.34
 */

package com.mtv.app.shopme.nav.route.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.common.navbar.customer.CustomerDestinations
import com.mtv.app.shopme.feature.customer.contract.ChatListEffect
import com.mtv.app.shopme.feature.customer.contract.ChatListEvent
import com.mtv.app.shopme.feature.customer.presentation.ChatListViewModel
import com.mtv.app.shopme.feature.customer.ui.ChatListScreen

@Composable
fun ChatListRoute(nav: NavController) {

    val vm: ChatListViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = ChatListEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent
    ) {
        BaseScreen(
            baseUiState = baseUiState,
            dismissDialog = { vm.onEvent(ChatListEvent.DismissDialog) }
        ) {
            ChatListScreen(
                state = uiState,
                event = { event -> vm.onEvent(event) }
            )
        }
    }
}

private fun handleEffect(
    nav: NavController,
    effect: ChatListEffect
) {
    when (effect) {
        is ChatListEffect.NavigateToChat -> {
            nav.navigate(CustomerDestinations.navigateToChat(effect.id))
        }
        ChatListEffect.NavigateBack -> nav.popBackStack()
    }
}
