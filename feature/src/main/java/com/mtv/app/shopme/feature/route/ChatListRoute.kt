/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: ListChatRoute.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 13.34
 */

package com.mtv.app.shopme.feature.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.contract.ChatListDataListener
import com.mtv.app.shopme.feature.contract.ChatListEventListener
import com.mtv.app.shopme.feature.contract.ChatListNavigationListener
import com.mtv.app.shopme.feature.contract.ChatListStateListener
import com.mtv.app.shopme.feature.presentation.ChatListViewModel
import com.mtv.app.shopme.feature.ui.ChatListScreen
import com.mtv.app.shopme.nav.AppDestinations

@Composable
fun ChatListRoute(nav: NavController) {
    BaseRoute<ChatListViewModel, ChatListStateListener, ChatListDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            ChatListScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = chatListEvent(vm),
                uiNavigation = chatListNavigation(nav)
            )
        }
    }
}

private fun chatListEvent(vm: ChatListViewModel) = ChatListEventListener(
    onClickItem = { id -> }
)

private fun chatListNavigation(nav: NavController) = ChatListNavigationListener(
    onBack = { nav.popBackStack() },
    navigateToChat = { id ->
        nav.navigate(AppDestinations.CHAT_GRAPH)
    }
)
