/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AccountSettingsViewModel.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 22.59
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.customer.contract.SettingsEffect
import com.mtv.app.shopme.feature.customer.contract.SettingsEvent
import com.mtv.app.shopme.feature.customer.contract.SettingsUiState
import com.mtv.based.core.provider.utils.SessionManager
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : BaseEventViewModel<SettingsEvent, SettingsEffect>() {

    private val _state = MutableStateFlow(SettingsUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.Load -> {}
            is SettingsEvent.DismissDialog -> dismissDialog()

            is SettingsEvent.ToggleNotification -> toggleNotification(event.enabled)
            is SettingsEvent.ToggleDarkMode -> toggleDarkMode(event.enabled)

            is SettingsEvent.Logout -> logout()

            is SettingsEvent.ClickBack -> emitEffect(SettingsEffect.NavigateBack)
            is SettingsEvent.ClickSecurity -> emitEffect(SettingsEffect.NavigateSecurity)
            is SettingsEvent.ClickHelp -> emitEffect(SettingsEffect.NavigateHelp)
            is SettingsEvent.ClickNotification -> emitEffect(SettingsEffect.NavigateNotification)
        }
    }

    private fun toggleNotification(enabled: Boolean) {
        _state.update {
            it.copy(notificationEnabled = enabled)
        }
    }

    private fun toggleDarkMode(enabled: Boolean) {
        _state.update {
            it.copy(darkMode = enabled)
        }
    }

    private fun logout() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }

                sessionManager.logout()

                _state.update { it.copy(isLoading = false) }

                emitEffect(SettingsEffect.LogoutSuccess)
            } catch (e: Exception) {
                showError(
                    UiError.Unknown(message = e.message ?: ErrorMessages.GENERIC_ERROR)
                )
            }
        }
    }

    private fun showError(error: UiError) {
        setDialog(
            UiDialog.Center(
                state = DialogStateV1(
                    type = DialogType.ERROR,
                    title = ErrorMessages.GENERIC_ERROR,
                    message = error.message
                ),
                onPrimary = { dismissDialog() }
            )
        )
    }
}