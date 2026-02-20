/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotifRoute.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 14.59
 */

package com.mtv.app.shopme.feature.customer.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.NotifDataListener
import com.mtv.app.shopme.feature.customer.contract.NotifEventListener
import com.mtv.app.shopme.feature.customer.contract.NotifNavigationListener
import com.mtv.app.shopme.feature.customer.contract.NotifStateListener
import com.mtv.app.shopme.feature.customer.presentation.NotifViewModel
import com.mtv.app.shopme.feature.customer.ui.NotificationScreen

@Composable
fun NotifRoute(nav: NavController) {
    BaseRoute<NotifViewModel, NotifStateListener, NotifDataListener> { vm, base, uiState, uiData ->
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

private fun notificationEvent(vm: NotifViewModel) = NotifEventListener(
    onNotificationClicked = {},
    onGetNotification = {},
    onClearNotification = {},
    onDismissActiveDialog = {}
)

/*private fun notificationEvent(vm: NotificationViewModel) = NotificationEventListener(
    onNotificationClicked = {},
    onGetNotification = vm::getLocalNotification,
    onClearNotification = vm::doClearNotification,
    onDismissActiveDialog = vm::doDismissActiveDialog
)*/

private fun notificationNavigation(nav: NavController) = NotifNavigationListener(
    onBack = {}
)