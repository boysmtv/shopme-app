/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.nav.route.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.ChatEffect
import com.mtv.app.shopme.feature.customer.contract.ChatEvent
import com.mtv.app.shopme.feature.customer.presentation.ChatViewModel
import com.mtv.app.shopme.feature.customer.ui.ChatScreen

@Composable
fun ChatRoute(nav: NavController) {

    val vm: ChatViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = ChatEvent.Load,
        onEffect = { handleChatEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(ChatEvent.DismissDialog) }
            ) {
                ChatScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleChatEffect(
    nav: NavController,
    effect: ChatEffect
) {
    when (effect) {
        ChatEffect.NavigateBack -> nav.popBackStack()
    }
}