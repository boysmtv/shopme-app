/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: LoginViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.49
 */

package com.mtv.app.shopme.feature.auth.presentation

import com.mtv.app.shopme.common.ConstantPreferences.ACCESS_TOKEN
import com.mtv.app.shopme.common.ConstantPreferences.REMEMBERED_LOGIN_EMAIL
import com.mtv.app.shopme.common.ConstantPreferences.USER_ROLE
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.param.LoginParam
import com.mtv.app.shopme.domain.usecase.GetLoginUseCase
import com.mtv.app.shopme.feature.auth.contract.*
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

    init {
        val savedEmail = securePrefs.getString(REMEMBERED_LOGIN_EMAIL).orEmpty()
        if (savedEmail.isNotBlank()) {
            _state.update { it.copy(email = savedEmail, rememberEmail = true) }
        }
    }

    override fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> onEmailChange(event)
            is LoginEvent.OnPasswordChange -> onPasswordChange(event)
            is LoginEvent.OnRememberEmailChange -> onRememberEmailChange(event)
            LoginEvent.OnLoginClick -> doLogin()
            LoginEvent.DismissDialog -> dismissDialog()
            LoginEvent.NavigateToRegister -> emitEffect(LoginEffect.NavigateToRegister)
            LoginEvent.NavigateToForgotPassword -> emitEffect(LoginEffect.NavigateToForgotPassword)
        }
    }

    private fun onEmailChange(event: LoginEvent.OnEmailChange) {
        _state.update { it.copy(email = event.value) }
    }

    private fun onPasswordChange(event: LoginEvent.OnPasswordChange) {
        _state.update { it.copy(password = event.value) }
    }

    private fun onRememberEmailChange(event: LoginEvent.OnRememberEmailChange) {
        _state.update { it.copy(rememberEmail = event.value) }
        if (!event.value) {
            securePrefs.putString(REMEMBERED_LOGIN_EMAIL, "")
        }
    }

    private fun doLogin() {
        val snapshot = _state.value
        val submittedEmail = snapshot.email
        val submittedPassword = snapshot.password

        if (submittedEmail.isBlank()) {
            _state.update { it.copy(login = LoadState.Error(UiError.Validation(message = "Email tidak boleh kosong"))) }
            return
        }
        if (submittedPassword.isBlank()) {
            _state.update { it.copy(login = LoadState.Error(UiError.Validation(message = "Password tidak boleh kosong"))) }
            return
        }

        observeDataFlow(
            flow = loginUseCase(
                LoginParam(
                    email = submittedEmail,
                    password = submittedPassword
                )
            ),
            onLoad = {
                _state.update {
                    it.copy(
                        email = submittedEmail,
                        password = submittedPassword,
                        login = LoadState.Loading
                    )
                }
            },
            onSuccess = { data ->
                _state.update {
                    it.copy(
                        email = submittedEmail,
                        password = submittedPassword,
                        login = LoadState.Success(data)
                    )
                }
                securePrefs.putString(
                    REMEMBERED_LOGIN_EMAIL,
                    if (snapshot.rememberEmail) submittedEmail else ""
                )
                securePrefs.putString(
                    ACCESS_TOKEN,
                    data.accessToken
                )
                securePrefs.putString(
                    USER_ROLE,
                    data.role.uppercase()
                )
                if (data.role.equals("SELLER", ignoreCase = true)) {
                    emitEffect(LoginEffect.NavigateToSellerDashboard)
                } else {
                    emitEffect(LoginEffect.NavigateToHome)
                }
            },
            onError = { uiError ->
                _state.update {
                    it.copy(
                        email = submittedEmail,
                        password = submittedPassword,
                        login = LoadState.Error(uiError)
                    )
                }
                showError(uiError)
            }
        )
    }

    private fun showError(error: UiError) {
        setDialog(
            UiDialog.Center(
                state = DialogStateV1(
                    type = DialogType.ERROR,
                    title = "Login gagal",
                    message = error.message
                ),
                onPrimary = { dismissDialog() }
            )
        )
    }

    private fun mapThrowableToUiError(throwable: Throwable?): UiError {
        return when (throwable) {
            is com.mtv.app.shopme.core.error.ApiException.Unauthorized ->
                UiError.Unauthorized(message = throwable.message ?: "")
            is com.mtv.app.shopme.core.error.ApiException.Forbidden ->
                UiError.Forbidden(message = throwable.message ?: "")
            is com.mtv.app.shopme.core.error.ApiException.Validation ->
                UiError.Validation(message = throwable.message ?: "")
            is java.io.IOException ->
                UiError.Network(message = throwable.message ?: "")
            else -> UiError.Unknown(
                message = throwable?.message ?: com.mtv.based.core.network.utils.ErrorMessages.GENERIC_ERROR
            )
        }
    }

}
