/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatSupportRoute.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 11.13
 */

package com.mtv.app.shopme.feature.customer.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.ChatSupportDataListener
import com.mtv.app.shopme.feature.customer.contract.ChatSupportEventListener
import com.mtv.app.shopme.feature.customer.contract.ChatSupportNavigationListener
import com.mtv.app.shopme.feature.customer.contract.ChatSupportStateListener
import com.mtv.app.shopme.feature.customer.presentation.ChatSupportViewModel
import com.mtv.app.shopme.feature.customer.ui.ChatSupportScreen

@Composable
fun ChatSupportRoute(nav: NavController) {
    BaseRoute<ChatSupportViewModel, ChatSupportStateListener, ChatSupportDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            ChatSupportScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = ChatSupportEventListener(
                    onMessageChange = vm::onMessageChange,
                    onSendMessage = vm::sendMessage
                ),
                uiNavigation = ChatSupportNavigationListener(
                    onBack = { nav.popBackStack() }
                )
            )
        }
    }
}