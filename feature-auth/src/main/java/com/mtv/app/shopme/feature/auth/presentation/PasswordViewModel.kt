/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: PasswordViewModel.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 09.24
 */

package com.mtv.app.shopme.feature.auth.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.auth.contract.PasswordDataListener
import com.mtv.app.shopme.feature.auth.contract.PasswordStateListener
import com.mtv.based.core.network.utils.ResourceFirebase
import com.mtv.based.core.network.utils.UiErrorFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PasswordViewModel @Inject constructor() :
    BaseViewModel(),
    UiOwner<PasswordStateListener, PasswordDataListener> {

    override val uiState = MutableStateFlow(PasswordStateListener())
    override val uiData = MutableStateFlow(PasswordDataListener())

    fun updateCurrentPassword(value: String) {
        uiData.value = uiData.value.copy(currentPassword = value)
    }

    fun updateNewPassword(value: String) {
        uiData.value = uiData.value.copy(newPassword = value)
    }

    fun updateConfirmPassword(value: String) {
        uiData.value = uiData.value.copy(confirmPassword = value)
    }

    fun changePassword(onSuccess: () -> Unit) {
        viewModelScope.launch {

            uiState.value =
                uiState.value.copy(changePasswordState = ResourceFirebase.Loading)

            delay(700)

            val data = uiData.value

            if (data.newPassword == data.confirmPassword &&
                data.currentPassword.isNotEmpty()
            ) {
                uiState.value =
                    uiState.value.copy(changePasswordState = ResourceFirebase.Success("Password Updated"))
                onSuccess()
            } else {
                uiState.value =
                    uiState.value.copy(
                        changePasswordState = ResourceFirebase.Error(
                            UiErrorFirebase.Unknown("Password not match")
                        )
                    )
            }
        }
    }
}