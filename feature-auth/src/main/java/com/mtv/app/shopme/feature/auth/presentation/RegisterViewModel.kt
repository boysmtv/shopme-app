/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: RegisterViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.52
 */

package com.mtv.app.shopme.feature.auth.presentation

import com.mtv.based.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.common.valueFlowOf
import com.mtv.app.shopme.data.remote.request.RegisterRequest
import com.mtv.app.shopme.domain.usecase.RegisterUseCase
import com.mtv.app.shopme.feature.auth.contract.RegisterDataListener
import com.mtv.app.shopme.feature.auth.contract.RegisterDialog
import com.mtv.app.shopme.feature.auth.contract.RegisterStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : BaseViewModel(), UiOwner<RegisterStateListener, RegisterDataListener> {

    override val uiState = MutableStateFlow(RegisterStateListener())
    override val uiData = MutableStateFlow(RegisterDataListener())

    fun updateName(value: String) {
        uiData.value = uiData.value.copy(name = value)
    }

    fun updateEmail(value: String) {
        uiData.value = uiData.value.copy(email = value)
    }

    fun updatePassword(value: String) {
        uiData.value = uiData.value.copy(password = value)
    }

    fun doRegister(
        name: String,
        email: String,
        password: String
    ) {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.registerState },
                set = { state -> copy(registerState = state) }
            ),
            block = {
                registerUseCase(
                    RegisterRequest(
                        name = name,
                        email = email,
                        password = password
                    )
                )
            },
            onSuccess = {
                uiState.update {
                    it.copy(
                        activeDialog = RegisterDialog.Success
                    )
                }
            }
        )
    }

    fun doDismissActiveDialog() {
        uiState.update {
            it.copy(
                activeDialog = null,
            )
        }
    }
}