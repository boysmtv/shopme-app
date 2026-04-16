/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: LoginContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.49
 */

package com.mtv.app.shopme.feature.auth.contract

import com.mtv.app.shopme.domain.model.Login
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class LoginUiState(
    val email: String = EMPTY_STRING,
    val password: String = EMPTY_STRING,
    val login: LoadState<Login> = LoadState.Idle
)

sealed class LoginEvent {
    data class OnEmailChange(val value: String) : LoginEvent()
    data class OnPasswordChange(val value: String) : LoginEvent()
    data object OnLoginClick : LoginEvent()
    data object DismissDialog : LoginEvent()
    data object NavigateToRegister : LoginEvent()
    data object NavigateToForgotPassword : LoginEvent()
}

sealed class LoginEffect {
    data object NavigateToHome : LoginEffect()
    data object NavigateToRegister : LoginEffect()
    data object NavigateToForgotPassword : LoginEffect()
}