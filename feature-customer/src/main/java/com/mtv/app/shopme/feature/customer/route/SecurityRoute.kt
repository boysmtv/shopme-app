/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SecurityRoute.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.11
 */

package com.mtv.app.shopme.feature.customer.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.SecurityDataListener
import com.mtv.app.shopme.feature.customer.contract.SecurityEventListener
import com.mtv.app.shopme.feature.customer.contract.SecurityNavigationListener
import com.mtv.app.shopme.feature.customer.contract.SecurityStateListener
import com.mtv.app.shopme.feature.customer.presentation.SecurityViewModel
import com.mtv.app.shopme.feature.customer.ui.SecurityScreen
import com.mtv.app.shopme.nav.AuthDestinations

@Composable
fun SecurityRoute(nav: NavController) {
    BaseRoute<SecurityViewModel, SecurityStateListener, SecurityDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            SecurityScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = securityEvent(vm),
                uiNavigation = securityNavigation(nav)
            )
        }
    }
}

private fun securityEvent(vm: SecurityViewModel) = SecurityEventListener(
    onToggleBiometric = vm::toggleBiometric,
    onLogoutAllDevice = vm::logoutAllDevice,
    onDeleteAccount = vm::deleteAccount
)

private fun securityNavigation(nav: NavController) = SecurityNavigationListener(
    onBack = { nav.popBackStack() },
    onChangePassword = { nav.navigate(AuthDestinations.PASSWORD_GRAPH) },
    onChangePin = { nav.navigate(AuthDestinations.CHANGE_PIN_GRAPH) }
)