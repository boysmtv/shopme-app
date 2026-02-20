/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ResetRoute.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.54
 */

package com.mtv.app.shopme.feature.auth.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.auth.contract.ResetDataListener
import com.mtv.app.shopme.feature.auth.contract.ResetEventListener
import com.mtv.app.shopme.feature.auth.contract.ResetNavigationListener
import com.mtv.app.shopme.feature.auth.contract.ResetStateListener
import com.mtv.app.shopme.feature.auth.presentation.ResetViewModel
import com.mtv.app.shopme.feature.auth.ui.ResetScreen

@Composable
fun ResetRoute(nav: NavController) {
    BaseRoute<ResetViewModel, ResetStateListener, ResetDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            ResetScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = ResetEventListener(
                    onEmailChange = vm::updateEmail,
                    onResetClick = {
                        nav.popBackStack()
                    }
                ),
                uiNavigation = ResetNavigationListener(
                    onNavigateToLogin = { nav.popBackStack() },
                    onBack = { nav.popBackStack() }
                )
            )
        }
    }
}