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
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.shopme.common.ConstantPreferences.ACCESS_TOKEN
import com.mtv.app.shopme.common.ConstantPreferences.SPLASH_RESPONSE
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.common.valueFlowOf
import com.mtv.app.shopme.data.remote.request.LoginRequest
import com.mtv.app.shopme.data.remote.request.RegisterRequest
import com.mtv.app.shopme.domain.usecase.LoginUseCase
import com.mtv.app.shopme.feature.auth.contract.LoginDataListener
import com.mtv.app.shopme.feature.auth.contract.LoginStateListener
import com.mtv.app.shopme.feature.auth.contract.RegisterDialog
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.ResourceFirebase
import com.mtv.based.core.network.utils.UiErrorFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.update

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val securePrefs: SecurePrefs,
    private val loginUseCase: LoginUseCase
) : BaseViewModel(), UiOwner<LoginStateListener, LoginDataListener> {

    override val uiState = MutableStateFlow(LoginStateListener())
    override val uiData = MutableStateFlow(LoginDataListener())

    fun updateEmail(value: String) {
        uiData.value = uiData.value.copy(email = value)
    }

    fun updatePassword(value: String) {
        uiData.value = uiData.value.copy(password = value)
    }

    fun doLogin(
        email: String,
        password: String
    ) {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.loginState },
                set = { state -> copy(loginState = state) }
            ),
            block = {
                loginUseCase(
                    LoginRequest(
                        email = email,
                        password = password
                    )
                )
            },
            onSuccess = { data ->
                securePrefs.putString(ACCESS_TOKEN, data.data?.accessToken.orEmpty())
            }
        )
    }
}