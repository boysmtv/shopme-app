/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotificationRoute.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.26
 */

package com.mtv.app.shopme.nav.route.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.NotificationEffect
import com.mtv.app.shopme.feature.customer.contract.NotificationEvent
import com.mtv.app.shopme.feature.customer.presentation.NotificationViewModel
import com.mtv.app.shopme.feature.customer.ui.NotificationScreen

@Composable
fun NotificationRoute(nav: NavController) {

    val vm: NotificationViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = NotificationEvent.Load,
        onEffect = { handleNotificationEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(NotificationEvent.DismissDialog) }
            ) {
                NotificationScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleNotificationEffect(
    nav: NavController,
    effect: NotificationEffect
) {
    when (effect) {
        NotificationEffect.NavigateBack -> nav.popBackStack()
    }
}