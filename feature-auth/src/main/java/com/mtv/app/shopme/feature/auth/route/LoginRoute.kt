/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: LoginRoute.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.51
 */

package com.mtv.app.shopme.feature.auth.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.auth.contract.LoginDataListener
import com.mtv.app.shopme.feature.auth.contract.LoginEventListener
import com.mtv.app.shopme.feature.auth.contract.LoginNavigationListener
import com.mtv.app.shopme.feature.auth.contract.LoginStateListener
import com.mtv.app.shopme.feature.auth.presentation.LoginViewModel
import com.mtv.app.shopme.feature.auth.ui.LoginScreen
import com.mtv.app.shopme.nav.AuthDestinations
import com.mtv.app.shopme.nav.CustomerDestinations

@Composable
fun LoginRoute(nav: NavController) {
    BaseRoute<LoginViewModel, LoginStateListener, LoginDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            LoginScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = LoginEventListener(
                    onEmailChange = vm::updateEmail,
                    onPasswordChange = vm::updatePassword,
                    onLoginClick = {
                        nav.navigate(CustomerDestinations.HOME_GRAPH)
                    }
                ),
                uiNavigation = LoginNavigationListener(
                    onNavigateToRegister = { nav.navigate(AuthDestinations.REGISTER_GRAPH) },
                    onNavigateToForgotPassword = { nav.navigate(AuthDestinations.RESET_GRAPH) },
                    onBack = { nav.popBackStack() }
                )
            )
        }
    }
}