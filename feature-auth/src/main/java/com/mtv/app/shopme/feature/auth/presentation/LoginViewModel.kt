/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: LoginViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.49
 */

package com.mtv.app.shopme.feature.auth.presentation

import com.mtv.based.core.provider.based.BaseViewModel
import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.app.shopme.common.ConstantPreferences.ACCESS_TOKEN
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.common.valueFlowOf
import com.mtv.app.shopme.data.remote.request.LoginRequest
import com.mtv.app.shopme.domain.usecase.GetLoginUseCase
import com.mtv.app.shopme.feature.auth.contract.LoginDataListener
import com.mtv.app.shopme.feature.auth.contract.LoginStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val securePrefs: SecurePrefs,
    private val loginUseCase: GetLoginUseCase
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