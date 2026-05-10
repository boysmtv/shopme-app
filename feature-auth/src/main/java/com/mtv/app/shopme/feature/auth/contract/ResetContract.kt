/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ResetContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.54
 */

package com.mtv.app.shopme.feature.auth.contract

import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

enum class ResetStage {
    EMAIL,
    OTP,
    PASSWORD
}

data class ResetUiState(
    val email: String = EMPTY_STRING,
    val otp: String = EMPTY_STRING,
    val newPassword: String = EMPTY_STRING,
    val confirmPassword: String = EMPTY_STRING,
    val stage: ResetStage = ResetStage.EMAIL,
    val reset: LoadState<String> = LoadState.Idle
)

sealed class ResetEvent {
    data class OnEmailChange(val value: String) : ResetEvent()
    data class OnOtpChange(val value: String) : ResetEvent()
    data class OnNewPasswordChange(val value: String) : ResetEvent()
    data class OnConfirmPasswordChange(val value: String) : ResetEvent()
    data object OnResetClick : ResetEvent()
    data object OnBackClick : ResetEvent()
    data object DismissDialog : ResetEvent()
}

sealed class ResetEffect {
    data object NavigateBack : ResetEffect()
}
