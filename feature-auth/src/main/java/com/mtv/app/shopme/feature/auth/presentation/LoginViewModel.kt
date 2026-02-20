/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: LoginViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.49
 */

package com.mtv.app.shopme.feature.auth.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.auth.contract.LoginDataListener
import com.mtv.app.shopme.feature.auth.contract.LoginStateListener
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.ResourceFirebase
import com.mtv.based.core.network.utils.UiErrorFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() :
    BaseViewModel(),
    UiOwner<LoginStateListener, LoginDataListener> {

    override val uiState = MutableStateFlow(LoginStateListener())
    override val uiData = MutableStateFlow(LoginDataListener())

    fun updateEmail(value: String) {
        uiData.value = uiData.value.copy(email = value)
    }

    fun updatePassword(value: String) {
        uiData.value = uiData.value.copy(password = value)
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(loginState = ResourceFirebase.Loading)
            delay(800)

            if (uiData.value.email.isNotEmpty() &&
                uiData.value.password.isNotEmpty()
            ) {
                uiState.value =
                    uiState.value.copy(loginState = ResourceFirebase.Success("Success"))
                onSuccess()
            } else {
                uiState.value =
                    uiState.value.copy(loginState = ResourceFirebase.Error(UiErrorFirebase.Unknown("Invalid credentials")))
            }
        }
    }
}