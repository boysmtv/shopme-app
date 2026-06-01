/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: LoginViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.49
 */

package com.mtv.app.shopme.feature.auth.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.common.ConstantPreferences.ACCESS_TOKEN
import com.mtv.app.shopme.common.ConstantPreferences.REMEMBERED_LOGIN_EMAIL
import com.mtv.app.shopme.common.ConstantPreferences.USER_ROLE
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.param.LoginParam
import com.mtv.app.shopme.domain.usecase.GetLoginUseCase
import com.mtv.app.shopme.feature.auth.contract.*
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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

        viewModelScope.launch {
            loginUseCase(
                LoginParam(
                    email = submittedEmail,
                    password = submittedPassword
                )
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                email = submittedEmail,
                                password = submittedPassword,
                                login = LoadState.Loading
                            )
                        }
                    }

                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                email = submittedEmail,
                                password = submittedPassword,
                                login = LoadState.Success(resource.data)
                            )
                        }
                        securePrefs.putString(
                            REMEMBERED_LOGIN_EMAIL,
                            if (snapshot.rememberEmail) submittedEmail else ""
                        )
                        securePrefs.putString(
                            ACCESS_TOKEN,
                            resource.data.accessToken
                        )
                        securePrefs.putString(
                            USER_ROLE,
                            resource.data.role.uppercase()
                        )
                        if (resource.data.role.equals("SELLER", ignoreCase = true)) {
                            emitEffect(LoginEffect.NavigateToSellerDashboard)
                        } else {
                            emitEffect(LoginEffect.NavigateToHome)
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                email = submittedEmail,
                                password = submittedPassword,
                                login = LoadState.Error(resource.error)
                            )
                        }
                        showError(resource.error)
                    }

                    else -> Unit
                }
            }
        }
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

}
