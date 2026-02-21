/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChangePinViewModel.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.17
 */

package com.mtv.app.shopme.feature.auth.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.auth.contract.ChangePinDataListener
import com.mtv.app.shopme.feature.auth.contract.ChangePinStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ChangePinViewModel @Inject constructor() :
    BaseViewModel(),
    UiOwner<ChangePinStateListener, ChangePinDataListener> {

    override val uiState = MutableStateFlow(ChangePinStateListener())
    override val uiData = MutableStateFlow(ChangePinDataListener())

    fun updateOldPin(value: String) {
        uiData.value = uiData.value.copy(oldPin = value.take(6))
    }

    fun updateNewPin(value: String) {
        uiData.value = uiData.value.copy(newPin = value.take(6))
    }

    fun updateConfirmPin(value: String) {
        uiData.value = uiData.value.copy(confirmPin = value.take(6))
    }

    fun submit() {
        val data = uiData.value

        if (data.newPin != data.confirmPin) {
            uiState.value = uiState.value.copy(error = "PIN tidak sama")
            return
        }

        // TODO API Change PIN
    }
}