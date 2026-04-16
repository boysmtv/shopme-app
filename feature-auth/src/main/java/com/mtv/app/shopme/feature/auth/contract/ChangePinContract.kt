/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChangePinContract.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.16
 */

package com.mtv.app.shopme.feature.auth.contract

import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class ChangePinUiState(
    val oldPin: String = EMPTY_STRING,
    val newPin: String = EMPTY_STRING,
    val confirmPin: String = EMPTY_STRING,
    val changePin: LoadState<Unit> = LoadState.Idle
)

sealed class ChangePinEvent {
    data class OnOldPinChange(val value: String) : ChangePinEvent()
    data class OnNewPinChange(val value: String) : ChangePinEvent()
    data class OnConfirmPinChange(val value: String) : ChangePinEvent()
    data object OnSubmit : ChangePinEvent()
    data object DismissDialog : ChangePinEvent()
}

sealed class ChangePinEffect {
    data object NavigateBack : ChangePinEffect()
}