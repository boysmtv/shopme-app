/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.feature.customer.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.ChatDataListener
import com.mtv.app.shopme.feature.customer.contract.ChatEventListener
import com.mtv.app.shopme.feature.customer.contract.ChatNavigationListener
import com.mtv.app.shopme.feature.customer.contract.ChatStateListener
import com.mtv.app.shopme.feature.customer.presentation.ChatViewModel
import com.mtv.app.shopme.feature.customer.ui.ChatScreen

@Composable
fun ChatRoute(nav: NavController) {
    BaseRoute<ChatViewModel, ChatStateListener, ChatDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            ChatScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = chatEvent(vm),
                uiNavigation = chatNavigation(nav)
            )
        }
    }
}

private fun chatEvent(vm: ChatViewModel) = ChatEventListener(
    onDismissActiveDialog = { }
)

private fun chatNavigation(nav: NavController) = ChatNavigationListener(
    onBack = { nav.popBackStack() }
)
