/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: LoginRoute.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.51
 */

package com.mtv.app.shopme.nav.route.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.auth.contract.LoginEffect
import com.mtv.app.shopme.feature.auth.contract.LoginEvent
import com.mtv.app.shopme.feature.auth.presentation.LoginViewModel
import com.mtv.app.shopme.feature.auth.ui.LoginScreen
import com.mtv.app.shopme.nav.customer.CustomerNavActions

@Composable
fun LoginRoute(nav: NavController) {

    val vm: LoginViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(LoginEvent.DismissDialog) }
            ) {
                LoginScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleEffect(
    nav: NavController,
    effect: LoginEffect
) {
    when (effect) {
        LoginEffect.NavigateToHome -> CustomerNavActions.toHome(nav)
        LoginEffect.NavigateToSellerDashboard -> CustomerNavActions.toSeller(nav)
        LoginEffect.NavigateToRegister -> CustomerNavActions.toRegister(nav)
        LoginEffect.NavigateToForgotPassword -> CustomerNavActions.toForgetPassword(nav)
    }
}
