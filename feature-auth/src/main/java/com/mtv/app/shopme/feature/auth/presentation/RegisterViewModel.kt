/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: RegisterViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.52
 */

package com.mtv.app.shopme.feature.auth.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.auth.contract.RegisterDataListener
import com.mtv.app.shopme.feature.auth.contract.RegisterStateListener
import com.mtv.based.core.network.utils.ResourceFirebase
import com.mtv.based.core.network.utils.UiErrorFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() :
    BaseViewModel(),
    UiOwner<RegisterStateListener, RegisterDataListener> {

    override val uiState = MutableStateFlow(RegisterStateListener())
    override val uiData = MutableStateFlow(RegisterDataListener())

    fun updateEmail(value: String) {
        uiData.value = uiData.value.copy(email = value)
    }

    fun updatePassword(value: String) {
        uiData.value = uiData.value.copy(password = value)
    }

    fun updateConfirmPassword(value: String) {
        uiData.value = uiData.value.copy(confirmPassword = value)
    }

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState.value =
                uiState.value.copy(registerState = ResourceFirebase.Loading)

            delay(800)

            if (uiData.value.password == uiData.value.confirmPassword &&
                uiData.value.email.isNotEmpty()
            ) {
                uiState.value =
                    uiState.value.copy(registerState = ResourceFirebase.Success("Register Success"))
                onSuccess()
            } else {
                uiState.value =
                    uiState.value.copy(registerState = ResourceFirebase.Error(UiErrorFirebase.Unknown("Password not match")))
            }
        }
    }
}