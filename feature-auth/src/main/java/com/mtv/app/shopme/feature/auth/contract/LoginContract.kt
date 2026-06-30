/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: LoginContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.49
 */

package com.mtv.app.shopme.feature.auth.contract

import androidx.compose.runtime.Immutable
import com.mtv.app.shopme.domain.model.Login
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

@Immutable
data class LoginUiState(
    val email: String = EMPTY_STRING,
    val password: String = EMPTY_STRING,
    val rememberEmail: Boolean = false,
    val login: LoadState<Login> = LoadState.Idle,
    val dialog: LoginDialog? = null,
    val debugToast: String = ""
)

sealed class LoginEvent {
    data class OnEmailChange(val value: String) : LoginEvent()
    data class OnPasswordChange(val value: String) : LoginEvent()
    data class OnRememberEmailChange(val value: Boolean) : LoginEvent()
    data object OnLoginClick : LoginEvent()
    data object DismissDialog : LoginEvent()
    data object NavigateToRegister : LoginEvent()
    data object NavigateToForgotPassword : LoginEvent()
}

sealed class LoginEffect {
    data object NavigateToHome : LoginEffect()
    data object NavigateToSellerDashboard : LoginEffect()
    data object NavigateToRegister : LoginEffect()
    data object NavigateToForgotPassword : LoginEffect()
}

sealed class LoginDialog {
    data class Error(val message: String) : LoginDialog()
}
