/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: LoginViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.49
 */

package com.mtv.app.shopme.feature.auth.presentation

import com.mtv.app.shopme.common.ConstantPreferences.ACCESS_TOKEN
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.param.LoginParam
import com.mtv.app.shopme.domain.usecase.GetLoginUseCase
import com.mtv.app.shopme.feature.auth.contract.*
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val securePrefs: SecurePrefs,
    private val loginUseCase: GetLoginUseCase
) : BaseEventViewModel<LoginEvent, LoginEffect>() {

    private val _state = MutableStateFlow(LoginUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> {
                _state.update { it.copy(email = event.value) }
            }

            is LoginEvent.OnPasswordChange -> {
                _state.update { it.copy(password = event.value) }
            }

            LoginEvent.OnLoginClick -> doLogin()
            LoginEvent.DismissDialog -> dismissDialog()
            LoginEvent.NavigateToRegister -> emitEffect(LoginEffect.NavigateToRegister)
            LoginEvent.NavigateToForgotPassword -> emitEffect(LoginEffect.NavigateToForgotPassword)
        }
    }

    private fun doLogin() {
        val state = _state.value

        observeDataFlow(
            flow = loginUseCase(
                LoginParam(
                    email = state.email,
                    password = state.password
                )
            ),
            onState = { result ->
                _state.update { it.copy(login = result) }

                if (result is LoadState.Success) {
                    securePrefs.putString(
                        ACCESS_TOKEN,
                        result.data.accessToken
                    )
                    emitEffect(LoginEffect.NavigateToHome)
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