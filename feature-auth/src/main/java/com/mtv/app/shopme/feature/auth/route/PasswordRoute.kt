/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChangePasswordRoute.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 09.25
 */

package com.mtv.app.shopme.feature.auth.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.auth.contract.PasswordDataListener
import com.mtv.app.shopme.feature.auth.contract.PasswordEventListener
import com.mtv.app.shopme.feature.auth.contract.PasswordNavigationListener
import com.mtv.app.shopme.feature.auth.contract.PasswordStateListener
import com.mtv.app.shopme.feature.auth.presentation.PasswordViewModel
import com.mtv.app.shopme.feature.auth.ui.PasswordScreen

@Composable
fun PasswordRoute(nav: NavController) {
    BaseRoute<PasswordViewModel, PasswordStateListener, PasswordDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            PasswordScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = PasswordEventListener(
                    onCurrentPasswordChange = vm::updateCurrentPassword,
                    onNewPasswordChange = vm::updateNewPassword,
                    onConfirmPasswordChange = vm::updateConfirmPassword,
                    onSubmitClick = {
                        vm.changePassword {
                            nav.popBackStack()
                        }
                    }
                ),
                uiNavigation = PasswordNavigationListener(
                    onBack = { nav.popBackStack() }
                )
            )
        }
    }
}