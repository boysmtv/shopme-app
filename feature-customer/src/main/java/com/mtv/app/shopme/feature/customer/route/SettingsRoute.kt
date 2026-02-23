/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SettingsRoute.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.01
 */

package com.mtv.app.shopme.feature.customer.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.SettingsDataListener
import com.mtv.app.shopme.feature.customer.contract.SettingsEventListener
import com.mtv.app.shopme.feature.customer.contract.SettingsNavigationListener
import com.mtv.app.shopme.feature.customer.contract.SettingsStateListener
import com.mtv.app.shopme.feature.customer.presentation.SettingsViewModel
import com.mtv.app.shopme.feature.customer.ui.SettingsScreen
import com.mtv.app.shopme.nav.CustomerDestinations

@Composable
fun SettingsRoute(nav: NavController) {
    BaseRoute<SettingsViewModel, SettingsStateListener, SettingsDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            SettingsScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = settingsEvent(vm),
                uiNavigation = settingsNavigation(nav)
            )
        }
    }
}

private fun settingsEvent(vm: SettingsViewModel) =
    SettingsEventListener(
        onToggleNotification = vm::toggleNotification,
        onToggleDarkMode = vm::toggleDarkMode,
        onLogout = vm::logout
    )

private fun settingsNavigation(nav: NavController) =
    SettingsNavigationListener(
        onBack = { nav.popBackStack() },
        onSecurity = { nav.navigate(CustomerDestinations.SECURITY_GRAPH) },
        onHelp = { nav.navigate(CustomerDestinations.SUPPORT_GRAPH) },
        onNotification = { nav.navigate(CustomerDestinations.NOTIFICATION_GRAPH)}
    )