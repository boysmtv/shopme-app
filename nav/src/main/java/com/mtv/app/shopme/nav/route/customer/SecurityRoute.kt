/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SecurityRoute.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.11
 */

package com.mtv.app.shopme.nav.route.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.common.navbar.auth.AuthDestinations
import com.mtv.app.shopme.feature.customer.contract.SecurityEffect
import com.mtv.app.shopme.feature.customer.contract.SecurityEvent
import com.mtv.app.shopme.feature.customer.presentation.SecurityViewModel
import com.mtv.app.shopme.feature.customer.ui.SecurityScreen

@Composable
fun SecurityRoute(nav: NavController) {

    val vm: SecurityViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SecurityEvent.Load,
        onEffect = { handleSecurityEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(SecurityEvent.DismissDialog) }
            ) {
                SecurityScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleSecurityEffect(
    nav: NavController,
    effect: SecurityEffect
) {
    when (effect) {
        SecurityEffect.NavigateBack -> nav.popBackStack()

        SecurityEffect.NavigateChangePassword ->
            nav.navigate(AuthDestinations.PASSWORD_GRAPH)

        SecurityEffect.NavigateChangePin ->
            nav.navigate(AuthDestinations.CHANGE_PIN_GRAPH)

        SecurityEffect.LogoutSuccess -> {
            // contoh:
            // nav.navigate("login") { popUpTo(0) }
        }

        SecurityEffect.DeleteAccountSuccess -> {
            // contoh:
            // nav.navigate("register") { popUpTo(0) }
        }
    }
}