package com.mtv.app.shopme.feature.auth

import com.mtv.app.shopme.domain.param.LoginParam
import com.mtv.app.shopme.domain.usecase.GetLoginUseCase
import com.mtv.app.shopme.feature.auth.contract.LoginEvent
import com.mtv.app.shopme.feature.auth.presentation.LoginViewModel
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SecurePrefs
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val securePrefs: SecurePrefs = mockk(relaxed = true)
    private val loginUseCase: GetLoginUseCase = mockk()

    @Test
    fun `invalid credentials should keep login inputs`() = runTest {
        every {
            loginUseCase.invoke(LoginParam("buyer.demo@shopme.local", "wrongpass"))
        } returns flowOf(Resource.Error(UiError.Unauthorized("Credential salah")))

        val vm = LoginViewModel(securePrefs, loginUseCase)

        vm.onEvent(LoginEvent.OnEmailChange("buyer.demo@shopme.local"))
        vm.onEvent(LoginEvent.OnPasswordChange("wrongpass"))
        vm.onEvent(LoginEvent.OnLoginClick)
        advanceUntilIdle()

        assertEquals("buyer.demo@shopme.local", vm.uiState.value.email)
        assertEquals("wrongpass", vm.uiState.value.password)
        assertTrue(vm.uiState.value.login is LoadState.Error)
        assertTrue(vm.baseUiState.value.dialog != null)
    }
}
