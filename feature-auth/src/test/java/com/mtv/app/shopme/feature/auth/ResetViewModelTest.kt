package com.mtv.app.shopme.feature.auth

import com.mtv.app.shopme.domain.param.ForgotPasswordParam
import com.mtv.app.shopme.domain.param.ResetPasswordParam
import com.mtv.app.shopme.domain.param.VerifyOtpParam
import com.mtv.app.shopme.domain.usecase.ForgotPasswordUseCase
import com.mtv.app.shopme.domain.usecase.ResetPasswordUseCase
import com.mtv.app.shopme.domain.usecase.VerifyOtpUseCase
import com.mtv.app.shopme.feature.auth.contract.ResetEffect
import com.mtv.app.shopme.feature.auth.contract.ResetEvent
import com.mtv.app.shopme.feature.auth.contract.ResetStage
import com.mtv.app.shopme.feature.auth.presentation.ResetViewModel
import com.mtv.based.core.network.utils.Resource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ResetViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val forgotPasswordUseCase: ForgotPasswordUseCase = mockk()
    private val verifyOtpUseCase: VerifyOtpUseCase = mockk()
    private val resetPasswordUseCase: ResetPasswordUseCase = mockk()

    @Test
    fun `reset should send otp and move to otp stage`() = runTest {
        every { forgotPasswordUseCase.invoke(ForgotPasswordParam("dedy@mail.com")) } returns flowOf(Resource.Success(Unit))
        val vm = ResetViewModel(forgotPasswordUseCase, verifyOtpUseCase, resetPasswordUseCase)

        vm.onEvent(ResetEvent.OnEmailChange("dedy@mail.com"))
        vm.onEvent(ResetEvent.OnResetClick)
        advanceUntilIdle()

        assertEquals(ResetStage.OTP, vm.uiState.value.stage)
    }

    @Test
    fun `reset should verify otp and move to password stage`() = runTest {
        every { forgotPasswordUseCase.invoke(ForgotPasswordParam("dedy@mail.com")) } returns flowOf(Resource.Success(Unit))
        every { verifyOtpUseCase.invoke(VerifyOtpParam("dedy@mail.com", "123456")) } returns flowOf(Resource.Success(Unit))
        val vm = ResetViewModel(forgotPasswordUseCase, verifyOtpUseCase, resetPasswordUseCase)

        vm.onEvent(ResetEvent.OnEmailChange("dedy@mail.com"))
        vm.onEvent(ResetEvent.OnResetClick)
        advanceUntilIdle()
        vm.onEvent(ResetEvent.OnOtpChange("123456"))
        vm.onEvent(ResetEvent.OnResetClick)
        advanceUntilIdle()

        assertEquals(ResetStage.PASSWORD, vm.uiState.value.stage)
    }

    @Test
    fun `back should move between recovery stages before leaving screen`() = runTest {
        every { forgotPasswordUseCase.invoke(ForgotPasswordParam("dedy@mail.com")) } returns flowOf(Resource.Success(Unit))
        every { verifyOtpUseCase.invoke(VerifyOtpParam("dedy@mail.com", "123456")) } returns flowOf(Resource.Success(Unit))
        val vm = ResetViewModel(forgotPasswordUseCase, verifyOtpUseCase, resetPasswordUseCase)

        vm.onEvent(ResetEvent.OnEmailChange("dedy@mail.com"))
        vm.onEvent(ResetEvent.OnResetClick)
        advanceUntilIdle()
        vm.onEvent(ResetEvent.OnOtpChange("123456"))
        vm.onEvent(ResetEvent.OnResetClick)
        advanceUntilIdle()
        assertEquals(ResetStage.PASSWORD, vm.uiState.value.stage)

        vm.onEvent(ResetEvent.OnBackClick)
        assertEquals(ResetStage.OTP, vm.uiState.value.stage)

        vm.onEvent(ResetEvent.OnBackClick)
        assertEquals(ResetStage.EMAIL, vm.uiState.value.stage)
    }

    @Test
    fun `reset password stage should navigate back on success`() = runTest {
        every { forgotPasswordUseCase.invoke(ForgotPasswordParam("dedy@mail.com")) } returns flowOf(Resource.Success(Unit))
        every { verifyOtpUseCase.invoke(VerifyOtpParam("dedy@mail.com", "123456")) } returns flowOf(Resource.Success(Unit))
        every { resetPasswordUseCase.invoke(ResetPasswordParam("dedy@mail.com", "newpass")) } returns flowOf(Resource.Success(Unit))

        val vm = ResetViewModel(forgotPasswordUseCase, verifyOtpUseCase, resetPasswordUseCase)
        val effect = async { vm.effect.first() }

        vm.onEvent(ResetEvent.OnEmailChange("dedy@mail.com"))
        vm.onEvent(ResetEvent.OnResetClick)
        advanceUntilIdle()
        vm.onEvent(ResetEvent.OnOtpChange("123456"))
        vm.onEvent(ResetEvent.OnResetClick)
        advanceUntilIdle()
        vm.onEvent(ResetEvent.OnNewPasswordChange("newpass"))
        vm.onEvent(ResetEvent.OnConfirmPasswordChange("newpass"))
        vm.onEvent(ResetEvent.OnResetClick)

        advanceUntilIdle()

        assertEquals(ResetEffect.NavigateBack, effect.await())
    }
}
