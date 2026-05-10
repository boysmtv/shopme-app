package com.mtv.app.shopme.feature.auth

import com.mtv.app.shopme.domain.param.ChangePasswordParam
import com.mtv.app.shopme.domain.usecase.ChangePasswordUseCase
import com.mtv.app.shopme.feature.auth.contract.PasswordEffect
import com.mtv.app.shopme.feature.auth.contract.PasswordEvent
import com.mtv.app.shopme.feature.auth.presentation.PasswordViewModel
import com.mtv.based.core.network.utils.Resource
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
        assertTrue(vm.uiState.value.changePassword is com.mtv.based.core.network.utils.LoadState.Success)
    }

    @Test fun `back click should navigate back`() = runTest {
        val vm = PasswordViewModel(useCase)
        val effect = async { vm.effect.first() }

        vm.onEvent(PasswordEvent.OnBackClick)
        advanceUntilIdle()

        assertEquals(PasswordEffect.NavigateBack, effect.await())
    }
}
