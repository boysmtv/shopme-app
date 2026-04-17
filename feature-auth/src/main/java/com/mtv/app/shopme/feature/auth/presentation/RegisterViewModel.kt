/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: RegisterViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.52
 */

package com.mtv.app.shopme.feature.auth.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.param.RegisterParam
import com.mtv.app.shopme.domain.usecase.GetRegisterUseCase
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
class RegisterViewModel @Inject constructor(
    private val registerUseCase: GetRegisterUseCase
) : BaseEventViewModel<RegisterEvent, RegisterEffect>() {

    private val _state = MutableStateFlow(RegisterUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.OnNameChange -> {
                _state.update { it.copy(name = event.value) }
            }

            is RegisterEvent.OnEmailChange -> {
                _state.update { it.copy(email = event.value) }
            }

            is RegisterEvent.OnPasswordChange -> {
                _state.update { it.copy(password = event.value) }
            }

            RegisterEvent.DismissDialog -> dismissDialog()
            is RegisterEvent.OnRegisterClick -> doRegister()
            RegisterEvent.OnLoginClick -> emitEffect(RegisterEffect.NavigateToLogin)
        }
    }

    private fun doRegister() {
        val state = _state.value

        observeDataFlow(
            flow = registerUseCase(
                RegisterParam(
                    name = state.name,
                    email = state.email,
                    password = state.password
                )
            ),
            onState = { result ->
                _state.update { it.copy(register = result) }

                if (result is LoadState.Success) {
                    _state.update {
                        it.copy(activeDialog = RegisterDialog.Success)
                    }
                }
            },
            onError = {
                showError(it)
            }
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