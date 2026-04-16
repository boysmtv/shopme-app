/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChangePinViewModel.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.17
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
class ChangePinViewModel @Inject constructor() :
    BaseEventViewModel<ChangePinEvent, ChangePinEffect>() {

    private val _state = MutableStateFlow(ChangePinUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: ChangePinEvent) {
        when (event) {
            is ChangePinEvent.OnOldPinChange -> {
                _state.update { it.copy(oldPin = event.value.take(6)) }
            }

            is ChangePinEvent.OnNewPinChange -> {
                _state.update { it.copy(newPin = event.value.take(6)) }
            }

            is ChangePinEvent.OnConfirmPinChange -> {
                _state.update { it.copy(confirmPin = event.value.take(6)) }
            }

            ChangePinEvent.OnSubmit -> submit()

            ChangePinEvent.DismissDialog -> dismissDialog()
        }
    }

    private fun submit() {
        val state = _state.value

        if (state.newPin != state.confirmPin) {
            showError("PIN tidak sama")
            return
        }

        if (state.newPin.length < 6) {
            showError("PIN harus 6 digit")
            return
        }

        // sementara mock success (nanti connect API)
        _state.update {
            it.copy(changePin = LoadState.Success(Unit))
        }

        emitEffect(ChangePinEffect.NavigateBack)
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