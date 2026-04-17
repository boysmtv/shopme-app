/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: RegisterContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.52
 */

package com.mtv.app.shopme.feature.auth.contract

import com.mtv.app.shopme.domain.model.Register
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class RegisterUiState(
    val name: String = EMPTY_STRING,
    val email: String = EMPTY_STRING,
    val password: String = EMPTY_STRING,
    val register: LoadState<Register> = LoadState.Idle,
    val activeDialog: RegisterDialog? = null
)

sealed class RegisterEvent {
    data class OnNameChange(val value: String) : RegisterEvent()
    data class OnEmailChange(val value: String) : RegisterEvent()
    data class OnPasswordChange(val value: String) : RegisterEvent()
    data class OnRegisterClick(val name: String, val email: String, val password: String) : RegisterEvent()
    data object OnLoginClick : RegisterEvent()
    data object DismissDialog : RegisterEvent()
}

sealed class RegisterEffect {
    data object NavigateToLogin : RegisterEffect()
}

sealed class RegisterDialog {
    data class Error(val message: String) : RegisterDialog()
    data object Success : RegisterDialog()
}