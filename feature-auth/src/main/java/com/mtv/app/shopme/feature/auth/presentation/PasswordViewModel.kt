/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: PasswordViewModel.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 09.24
 */

package com.mtv.app.shopme.feature.auth.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.param.ChangePasswordParam
import com.mtv.app.shopme.domain.usecase.ChangePasswordUseCase
import com.mtv.app.shopme.feature.auth.contract.*
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase
) : BaseEventViewModel<PasswordEvent, PasswordEffect>() {

    private val _state = MutableStateFlow(PasswordUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: PasswordEvent) {
        when (event) {
            is PasswordEvent.OnCurrentPasswordChange -> _state.update { it.copy(currentPassword = event.value) }
            is PasswordEvent.OnNewPasswordChange -> _state.update { it.copy(newPassword = event.value) }
            is PasswordEvent.OnConfirmPasswordChange -> _state.update { it.copy(confirmPassword = event.value) }
            PasswordEvent.OnSubmitClick -> changePassword()
            PasswordEvent.OnBackClick -> emitEffect(PasswordEffect.NavigateBack)
            PasswordEvent.DismissDialog -> dismissDialog()
        }
    }

    private fun changePassword() {
        val state = _state.value
        if (state.currentPassword.isEmpty()) {
            showError(UiError.Validation(message = "Password lama kosong"))
            return
        }
        if (state.newPassword.length < 6) {
            showError(UiError.Validation(message = "Password baru minimal 6 karakter"))
            return
        }
        if (state.newPassword != state.confirmPassword) {
            showError(UiError.Validation(message = "Password tidak sama"))
            return
        }

        observeDataFlow(
            flow = changePasswordUseCase(ChangePasswordParam(state.currentPassword, state.newPassword)),
            onState = { result ->
                _state.update { it.copy(changePassword = when (result) {
                    is LoadState.Loading -> LoadState.Loading
                    is LoadState.Success -> LoadState.Success("Password Updated")
                    is LoadState.Error -> LoadState.Error(result.error)
                    else -> LoadState.Idle
                }) }
                if (result is LoadState.Success) emitEffect(PasswordEffect.NavigateBack)
            },
            onError = ::showError
        )
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
