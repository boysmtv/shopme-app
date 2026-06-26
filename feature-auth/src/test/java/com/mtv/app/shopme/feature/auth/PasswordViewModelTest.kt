package com.mtv.app.shopme.feature.auth

import com.mtv.app.shopme.domain.param.ChangePasswordParam
import com.mtv.app.shopme.domain.usecase.ChangePasswordUseCase
import com.mtv.app.shopme.feature.auth.contract.PasswordEffect
import com.mtv.app.shopme.feature.auth.contract.PasswordEvent
import com.mtv.app.shopme.feature.auth.presentation.PasswordViewModel
import com.mtv.app.shopme.core.error.ApiException
import com.mtv.app.shopme.domain.model.Resource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.IOException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PasswordViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()
    private val useCase: ChangePasswordUseCase = mockk()

    @Test fun `change password should call backend usecase`() = runTest {
        every { useCase.invoke(ChangePasswordParam("oldpass", "newpass")) } returns flowOf(Resource.Success(Unit))
        val vm = PasswordViewModel(useCase)
        vm.onEvent(PasswordEvent.OnCurrentPasswordChange("oldpass"))
        vm.onEvent(PasswordEvent.OnNewPasswordChange("newpass"))
        vm.onEvent(PasswordEvent.OnConfirmPasswordChange("newpass"))
        vm.onEvent(PasswordEvent.OnSubmitClick)
        advanceUntilIdle()
        assertTrue(vm.uiState.value.changePassword is com.mtv.based.core.network.utils.LoadState.Success)
    }

    @Test fun `back click should navigate back`() = runTest {
        val vm = PasswordViewModel(useCase)
        val effect = async { vm.effect.first() }

        vm.onEvent(PasswordEvent.OnBackClick)
        advanceUntilIdle()

        assertEquals(PasswordEffect.NavigateBack, effect.await())
    }

    @Test
    fun `change password should show validation error on mismatch`() = runTest {
        val vm = PasswordViewModel(useCase)
        vm.onEvent(PasswordEvent.OnCurrentPasswordChange("oldpass"))
        vm.onEvent(PasswordEvent.OnNewPasswordChange("newpass"))
        vm.onEvent(PasswordEvent.OnConfirmPasswordChange("different"))
        vm.onEvent(PasswordEvent.OnSubmitClick)
        advanceUntilIdle()
        assertTrue(vm.uiState.value.changePassword is com.mtv.based.core.network.utils.LoadState.Idle)
        verify(exactly = 0) { useCase.invoke(any()) }
    }

    @Test
    fun `change password should handle Unauthorized error`() = runTest {
        every { useCase.invoke(ChangePasswordParam("oldpass", "newpass")) } returns flowOf(Resource.Error(ApiException.Unauthorized()))
        val vm = PasswordViewModel(useCase)
        vm.onEvent(PasswordEvent.OnCurrentPasswordChange("oldpass"))
        vm.onEvent(PasswordEvent.OnNewPasswordChange("newpass"))
        vm.onEvent(PasswordEvent.OnConfirmPasswordChange("newpass"))
        vm.onEvent(PasswordEvent.OnSubmitClick)
        advanceUntilIdle()
        assertTrue(vm.uiState.value.changePassword is com.mtv.based.core.network.utils.LoadState.Error)
    }

    @Test
    fun `change password should handle network error`() = runTest {
        every { useCase.invoke(ChangePasswordParam("oldpass", "newpass")) } returns flowOf(Resource.Error(IOException("No internet")))
        val vm = PasswordViewModel(useCase)
        vm.onEvent(PasswordEvent.OnCurrentPasswordChange("oldpass"))
        vm.onEvent(PasswordEvent.OnNewPasswordChange("newpass"))
        vm.onEvent(PasswordEvent.OnConfirmPasswordChange("newpass"))
        vm.onEvent(PasswordEvent.OnSubmitClick)
        advanceUntilIdle()
        assertTrue(vm.uiState.value.changePassword is com.mtv.based.core.network.utils.LoadState.Error)
    }
}
