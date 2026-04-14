/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotifRoute.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 14.59
 */

package com.mtv.app.shopme.nav.route.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.NotifEffect
import com.mtv.app.shopme.feature.customer.contract.NotifEvent
import com.mtv.app.shopme.feature.customer.presentation.NotifViewModel
import com.mtv.app.shopme.feature.customer.ui.NotifScreen

@Composable
fun NotifRoute(nav: NavController) {

    val vm: NotifViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = NotifEvent.Load,
        onEffect = { handleNotifEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(NotifEvent.DismissDialog) }
            ) {
                NotifScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleNotifEffect(
    nav: NavController,
    effect: NotifEffect
) {
    when (effect) {
        NotifEffect.NavigateBack -> nav.popBackStack()
    }
}