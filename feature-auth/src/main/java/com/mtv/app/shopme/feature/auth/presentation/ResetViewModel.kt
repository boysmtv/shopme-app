/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ResetViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.54
 */

package com.mtv.app.shopme.feature.auth.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.param.ForgotPasswordParam
import com.mtv.app.shopme.domain.param.ResetPasswordParam
import com.mtv.app.shopme.domain.param.VerifyOtpParam
import com.mtv.app.shopme.domain.usecase.ForgotPasswordUseCase
import com.mtv.app.shopme.domain.usecase.ResetPasswordUseCase
import com.mtv.app.shopme.domain.usecase.VerifyOtpUseCase
import com.mtv.app.shopme.feature.auth.contract.*
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ResetViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : BaseEventViewModel<ResetEvent, ResetEffect>() {

    private val _state = MutableStateFlow(ResetUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: ResetEvent) {
        when (event) {
            is ResetEvent.OnEmailChange -> _state.update { it.copy(email = event.value) }
            is ResetEvent.OnOtpChange -> _state.update { it.copy(otp = event.value) }
            is ResetEvent.OnNewPasswordChange -> _state.update { it.copy(newPassword = event.value) }
            is ResetEvent.OnConfirmPasswordChange -> _state.update { it.copy(confirmPassword = event.value) }
            ResetEvent.OnResetClick -> submitCurrentStage()
            ResetEvent.DismissDialog -> dismissDialog()
            ResetEvent.OnBackClick -> handleBack()
        }
    }

    private fun submitCurrentStage() {
        when (_state.value.stage) {
            ResetStage.EMAIL -> sendOtp()
            ResetStage.OTP -> verifyOtp()
            ResetStage.PASSWORD -> resetPassword()
        }
    }

    private fun sendOtp() {
        val email = _state.value.email.trim()
        if (email.isBlank()) {
            showError(UiError.Validation(message = "Email tidak boleh kosong"))
            return
        }

        observeDataFlow(
            flow = forgotPasswordUseCase(ForgotPasswordParam(email)),
            onState = { state ->
                _state.update {
                    it.copy(
                        reset = when (state) {
                            is LoadState.Loading -> LoadState.Loading
                            is LoadState.Success -> LoadState.Success("OTP berhasil dikirim")
                            is LoadState.Error -> LoadState.Error(state.error)
                            else -> LoadState.Idle
                        },
                        stage = if (state is LoadState.Success) ResetStage.OTP else it.stage,
                        otp = if (state is LoadState.Success) "" else it.otp,
                        newPassword = if (state is LoadState.Success) "" else it.newPassword,
                        confirmPassword = if (state is LoadState.Success) "" else it.confirmPassword
                    )
                }
            },
            onError = ::showError
        )
    }

    private fun verifyOtp() {
        val state = _state.value
        if (state.email.trim().isBlank()) {
            showError(UiError.Validation(message = "Email tidak boleh kosong"))
            return
        }
        if (state.otp.trim().length < 4) {
            showError(UiError.Validation(message = "OTP tidak valid"))
            return
        }

        observeDataFlow(
            flow = verifyOtpUseCase(
                VerifyOtpParam(
                    email = state.email.trim(),
                    otp = state.otp.trim()
                )
            ),
            onState = { result ->
                _state.update {
                    it.copy(
                        reset = when (result) {
                            is LoadState.Loading -> LoadState.Loading
                            is LoadState.Success -> LoadState.Success("OTP terverifikasi")
                            is LoadState.Error -> LoadState.Error(result.error)
                            else -> LoadState.Idle
                        },
                        stage = if (result is LoadState.Success) ResetStage.PASSWORD else it.stage,
                        newPassword = if (result is LoadState.Success) "" else it.newPassword,
                        confirmPassword = if (result is LoadState.Success) "" else it.confirmPassword
                    )
                }
            },
            onError = ::showError
        )
    }

    private fun resetPassword() {
        val state = _state.value
        val email = state.email.trim()
        if (email.isBlank()) {
            showError(UiError.Validation(message = "Email tidak boleh kosong"))
            return
        }
        if (state.newPassword.length < 6) {
            showError(UiError.Validation(message = "Password baru minimal 6 karakter"))
            return
        }
        if (state.newPassword != state.confirmPassword) {
            showError(UiError.Validation(message = "Password tidak sama"))
            return
        }

        observeDataFlow(
            flow = resetPasswordUseCase(
                ResetPasswordParam(
                    email = email,
                    newPassword = state.newPassword
                )
            ),
            onState = { result ->
                _state.update {
                    it.copy(
                        reset = when (result) {
                            is LoadState.Loading -> LoadState.Loading
                            is LoadState.Success -> LoadState.Success("Password berhasil diperbarui")
                            is LoadState.Error -> LoadState.Error(result.error)
                            else -> LoadState.Idle
                        }
                    )
                }
                if (result is LoadState.Success) {
                    emitEffect(ResetEffect.NavigateBack)
                }
            },
            onError = ::showError
        )
    }

    private fun handleBack() {
        when (_state.value.stage) {
            ResetStage.EMAIL -> emitEffect(ResetEffect.NavigateBack)
            ResetStage.OTP -> _state.update {
                it.copy(
                    stage = ResetStage.EMAIL,
                    otp = "",
                    reset = LoadState.Idle
                )
            }
            ResetStage.PASSWORD -> _state.update {
                it.copy(
                    stage = ResetStage.OTP,
                    newPassword = "",
                    confirmPassword = "",
                    reset = LoadState.Idle
                )
            }
        }
    }

    private fun showError(error: UiError) {
        _state.update { it.copy(reset = LoadState.Error(error)) }
        setDialog(
            UiDialog.Center(
                state = DialogStateV1(
                    type = DialogType.ERROR,
                    title = ErrorMessages.GENERIC_ERROR,
                    message = error.message
                ),
                onPrimary = { dismissDialog() }
            )
        )
    }
}
