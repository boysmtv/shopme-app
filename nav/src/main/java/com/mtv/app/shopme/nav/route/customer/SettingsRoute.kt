/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SettingsRoute.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.01
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
import com.mtv.app.shopme.feature.customer.contract.SettingsEffect
import com.mtv.app.shopme.feature.customer.contract.SettingsEvent
import com.mtv.app.shopme.feature.customer.presentation.SettingsViewModel
import com.mtv.app.shopme.feature.customer.ui.SettingsScreen

@Composable
fun SettingsRoute(nav: NavController) {

    val vm: SettingsViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SettingsEvent.Load,
        onEffect = { handleSettingsEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(SettingsEvent.DismissDialog) }
            ) {
                SettingsScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleSettingsEffect(
    nav: NavController,
    effect: SettingsEffect
) {
    when (effect) {
        SettingsEffect.NavigateBack -> nav.popBackStack()

        SettingsEffect.NavigateSecurity ->
            nav.navigate(CustomerDestinations.SECURITY_GRAPH)

        SettingsEffect.NavigateHelp ->
            nav.navigate(CustomerDestinations.SUPPORT_GRAPH)

        SettingsEffect.NavigateNotification ->
            nav.navigate(CustomerDestinations.NOTIFICATION_GRAPH)

        SettingsEffect.LogoutSuccess -> {
            // contoh:
            // nav.navigate("login") { popUpTo(0) }
        }
    }
}