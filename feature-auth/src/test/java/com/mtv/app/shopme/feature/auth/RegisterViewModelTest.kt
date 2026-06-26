@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.mtv.app.shopme.feature.auth

import com.mtv.app.shopme.domain.model.Register
import com.mtv.app.shopme.domain.param.RegisterParam
import com.mtv.app.shopme.domain.usecase.GetRegisterUseCase
import com.mtv.app.shopme.feature.auth.contract.RegisterDialog
import com.mtv.app.shopme.feature.auth.contract.RegisterEffect
import com.mtv.app.shopme.feature.auth.contract.RegisterEvent
import com.mtv.app.shopme.feature.auth.presentation.RegisterViewModel
import com.mtv.app.shopme.domain.model.Resource
import com.mtv.app.shopme.core.error.ApiException
import com.mtv.based.core.network.utils.LoadState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RegisterViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()
    private val registerUseCase: GetRegisterUseCase = mockk()

    @Test
    fun `successful registration should update state and show success dialog`() = runTest {
        every {
            registerUseCase.invoke(RegisterParam("John", "john@mail.com", "pass123"))
        } returns flowOf(Resource.Success(Register(success = true)))
        val vm = RegisterViewModel(registerUseCase)

        vm.onEvent(RegisterEvent.OnNameChange("John"))
        vm.onEvent(RegisterEvent.OnEmailChange("john@mail.com"))
        vm.onEvent(RegisterEvent.OnPasswordChange("pass123"))
        vm.onEvent(RegisterEvent.OnRegisterClick(name = "John", email = "john@mail.com", password = "pass123"))
        advanceUntilIdle()

        assertTrue(vm.uiState.value.register is LoadState.Success)
        assertEquals(RegisterDialog.Success, vm.uiState.value.activeDialog)
    }

    @Test
    fun `registration failure should show error state and dialog`() = runTest {
        every {
            registerUseCase.invoke(RegisterParam("John", "john@mail.com", "pass123"))
        } returns flowOf(Resource.Error(throwable = ApiException.Validation("Email already taken")))
        val vm = RegisterViewModel(registerUseCase)

        vm.onEvent(RegisterEvent.OnNameChange("John"))
        vm.onEvent(RegisterEvent.OnEmailChange("john@mail.com"))
        vm.onEvent(RegisterEvent.OnPasswordChange("pass123"))
        vm.onEvent(RegisterEvent.OnRegisterClick(name = "John", email = "john@mail.com", password = "pass123"))
        advanceUntilIdle()

        assertTrue(vm.uiState.value.register is LoadState.Error)
        assertTrue(vm.baseUiState.value.dialog != null)
    }

    @Test
    fun `login click should emit navigate to login effect`() = runTest {
        val vm = RegisterViewModel(registerUseCase)
        val effect = async { vm.effect.first() }

        vm.onEvent(RegisterEvent.OnLoginClick)
        advanceUntilIdle()

        assertEquals(RegisterEffect.NavigateToLogin, effect.await())
    }

    @Test
    fun `dismiss dialog should clear dialogs`() = runTest {
        val vm = RegisterViewModel(registerUseCase)

        vm.onEvent(RegisterEvent.DismissDialog)

        assertEquals(null, vm.baseUiState.value.dialog)
    }
}
