/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SecurityViewModel.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.11
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.customer.contract.SecurityEffect
import com.mtv.app.shopme.feature.customer.contract.SecurityEvent
import com.mtv.app.shopme.feature.customer.contract.SecurityUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SecurityViewModel @Inject constructor() :
    BaseEventViewModel<SecurityEvent, SecurityEffect>() {

    private val _state = MutableStateFlow(SecurityUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SecurityEvent) {
        when (event) {
            is SecurityEvent.Load -> {}
            is SecurityEvent.DismissDialog -> dismissDialog()

            is SecurityEvent.ToggleBiometric -> toggleBiometric(event.enabled)

            is SecurityEvent.LogoutAllDevice -> logoutAllDevice()
            is SecurityEvent.DeleteAccount -> deleteAccount()

            is SecurityEvent.ClickBack -> emitEffect(SecurityEffect.NavigateBack)
            is SecurityEvent.ClickChangePassword -> emitEffect(SecurityEffect.NavigateChangePassword)
            is SecurityEvent.ClickChangePin -> emitEffect(SecurityEffect.NavigateChangePin)
        }
    }

    private fun toggleBiometric(enabled: Boolean) {
        _state.update {
            it.copy(biometricEnabled = enabled)
        }
    }

    private fun logoutAllDevice() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            delay(800) // simulate API

            _state.update { it.copy(loading = false) }

            emitEffect(SecurityEffect.LogoutSuccess)
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            delay(800) // simulate API

            _state.update { it.copy(loading = false) }

            emitEffect(SecurityEffect.DeleteAccountSuccess)
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