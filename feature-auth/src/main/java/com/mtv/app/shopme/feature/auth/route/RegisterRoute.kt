/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: RegisterRoute.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.53
 */

package com.mtv.app.shopme.feature.auth.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.auth.contract.RegisterDataListener
import com.mtv.app.shopme.feature.auth.contract.RegisterEventListener
import com.mtv.app.shopme.feature.auth.contract.RegisterNavigationListener
import com.mtv.app.shopme.feature.auth.contract.RegisterStateListener
import com.mtv.app.shopme.feature.auth.presentation.RegisterViewModel
import com.mtv.app.shopme.feature.auth.ui.RegisterScreen
import com.mtv.app.shopme.nav.AuthDestinations

@Composable
fun RegisterRoute(nav: NavController) {
    BaseRoute<RegisterViewModel, RegisterStateListener, RegisterDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            RegisterScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = RegisterEventListener(
                    onEmailChange = vm::updateEmail,
                    onPasswordChange = vm::updatePassword,
                    onConfirmPasswordChange = vm::updateConfirmPassword,
                    onRegisterClick = {
                        nav.popBackStack()
                    }
                ),
                uiNavigation = RegisterNavigationListener(
                    onNavigateToLogin = { nav.popBackStack() },
                    onBack = { nav.popBackStack() }
                )
            )
        }
    }
}