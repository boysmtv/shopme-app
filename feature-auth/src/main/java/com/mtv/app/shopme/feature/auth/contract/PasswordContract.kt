/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: PasswordContract.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 09.24
 */

package com.mtv.app.shopme.feature.auth.contract

import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class PasswordUiState(
    val currentPassword: String = EMPTY_STRING,
    val newPassword: String = EMPTY_STRING,
    val confirmPassword: String = EMPTY_STRING,
    val changePassword: LoadState<String> = LoadState.Idle
)

sealed class PasswordEvent {
    data class OnCurrentPasswordChange(val value: String) : PasswordEvent()
    data class OnNewPasswordChange(val value: String) : PasswordEvent()
    data class OnConfirmPasswordChange(val value: String) : PasswordEvent()
    data object OnSubmitClick : PasswordEvent()
    data object OnBackClick : PasswordEvent()
    data object DismissDialog : PasswordEvent()
}

sealed class PasswordEffect {
    data object NavigateBack : PasswordEffect()
}
