/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotificationRoute.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.26
 */

package com.mtv.app.shopme.feature.customer.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.NotificationDataListener
import com.mtv.app.shopme.feature.customer.contract.NotificationEventListener
import com.mtv.app.shopme.feature.customer.contract.NotificationNavigationListener
import com.mtv.app.shopme.feature.customer.contract.NotificationStateListener
import com.mtv.app.shopme.feature.customer.presentation.NotificationViewModel
import com.mtv.app.shopme.feature.customer.ui.NotificationScreen

@Composable
fun NotificationRoute(nav: NavController) {
    BaseRoute<NotificationViewModel, NotificationStateListener, NotificationDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            NotificationScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = notificationEvent(vm),
                uiNavigation = notificationNavigation(nav)
            )
        }
    }
}

private fun notificationEvent(vm: NotificationViewModel) = NotificationEventListener(
    onToggleOrder = vm::toggleOrder,
    onTogglePromo = vm::togglePromo,
    onToggleChat = vm::toggleChat,
    onTogglePush = vm::togglePush,
    onToggleEmail = vm::toggleEmail
)

private fun notificationNavigation(nav: NavController) =
    NotificationNavigationListener(
        onBack = { nav.popBackStack() }
    )