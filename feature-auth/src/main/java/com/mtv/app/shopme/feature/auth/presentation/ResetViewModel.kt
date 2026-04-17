/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ResetViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.54
 */

package com.mtv.app.shopme.feature.auth.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.auth.contract.*
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ResetViewModel @Inject constructor() :
    BaseEventViewModel<ResetEvent, ResetEffect>() {

    private val _state = MutableStateFlow(ResetUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: ResetEvent) {
        when (event) {
            is ResetEvent.OnEmailChange -> {
                _state.update { it.copy(email = event.value) }
            }

            ResetEvent.OnResetClick -> resetPassword()

            ResetEvent.DismissDialog -> dismissDialog()
            ResetEvent.OnBackClick -> emitEffect(ResetEffect.NavigateBack)
        }
    }

    private fun resetPassword() {
        val email = _state.value.email

        if (email.isBlank()) {
            showError("Email tidak boleh kosong")
            return
        }

        // sementara mock success (nanti connect API)
        _state.update {
            it.copy(reset = LoadState.Success("Reset link dikirim"))
        }

        emitEffect(ResetEffect.NavigateBack)
    }

    private fun showError(message: String) {
        setDialog(
            UiDialog.Center(
                state = DialogStateV1(
                    type = DialogType.ERROR,
                    title = ErrorMessages.GENERIC_ERROR,
                    message = message
                ),
                onPrimary = { dismissDialog() }
            )
        )
    }
}