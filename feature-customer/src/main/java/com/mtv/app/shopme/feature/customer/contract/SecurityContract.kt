/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SecurityContract.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.11
 */

package com.mtv.app.shopme.feature.customer.contract

data class SecurityUiState(
    val loading: Boolean = false,
    val biometricEnabled: Boolean = false
)

sealed class SecurityEvent {
    object Load : SecurityEvent()
    object DismissDialog : SecurityEvent()

    data class ToggleBiometric(val enabled: Boolean) : SecurityEvent()

    object LogoutAllDevice : SecurityEvent()
    object DeleteAccount : SecurityEvent()

    object ClickBack : SecurityEvent()
    object ClickChangePassword : SecurityEvent()
    object ClickChangePin : SecurityEvent()
}

sealed class SecurityEffect {
    object NavigateBack : SecurityEffect()
    object NavigateChangePassword : SecurityEffect()
    object NavigateChangePin : SecurityEffect()

    object LogoutSuccess : SecurityEffect()
    object DeleteAccountSuccess : SecurityEffect()
}