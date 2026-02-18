/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotifRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.43
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

@Composable
fun NotifRoute(nav: NavController) {
    BaseRoute<NotifViewModel, NotifStateListener, NotifDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
//            NotifScreen(
//                uiState = uiState,
//                uiData = uiData,
//                uiEvent = notifEvent(vm),
//                uiNavigation = notifNavigation(nav)
//            )
        }
    }
}

private fun notifEvent(vm: NotifViewModel) = NotifEventListener(
    onDismissActiveDialog = { }
)

private fun notifNavigation(nav: NavController) = NotifNavigationListener(
    onBack = { nav.popBackStack() }
)
