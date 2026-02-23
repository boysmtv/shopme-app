/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SettingsContract.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 22.58
 */

package com.mtv.app.shopme.feature.customer.contract

data class SettingsStateListener(
    val isLoading: Boolean = false
)

data class SettingsDataListener(
    val notificationEnabled: Boolean = true,
    val darkMode: Boolean = false
)

data class SettingsEventListener(
    val onToggleNotification: (Boolean) -> Unit = {},
    val onToggleDarkMode: (Boolean) -> Unit = {},
    val onLogout: () -> Unit = {}
)

data class SettingsNavigationListener(
    val onBack: () -> Unit = {},
    val onSecurity: () -> Unit = {},
    val onHelp: () -> Unit = {},
    val onNotification: () -> Unit = {},
)