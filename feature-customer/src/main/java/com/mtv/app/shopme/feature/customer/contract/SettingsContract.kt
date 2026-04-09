/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SettingsContract.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 22.58
 */

package com.mtv.app.shopme.feature.customer.contract

data class SettingsUiState(
    val isLoading: Boolean = false,

    val notificationEnabled: Boolean = true,
    val darkMode: Boolean = false
)

sealed class SettingsEvent {
    object Load : SettingsEvent()
    object DismissDialog : SettingsEvent()

    data class ToggleNotification(val enabled: Boolean) : SettingsEvent()
    data class ToggleDarkMode(val enabled: Boolean) : SettingsEvent()

    object Logout : SettingsEvent()

    object ClickBack : SettingsEvent()
    object ClickSecurity : SettingsEvent()
    object ClickHelp : SettingsEvent()
    object ClickNotification : SettingsEvent()
}

sealed class SettingsEffect {
    object NavigateBack : SettingsEffect()
    object NavigateSecurity : SettingsEffect()
    object NavigateHelp : SettingsEffect()
    object NavigateNotification : SettingsEffect()

    object LogoutSuccess : SettingsEffect()
}