package com.mtv.app.shopme.feature.auth

import com.mtv.app.shopme.domain.param.ChangePinParam
import com.mtv.app.shopme.domain.usecase.ChangePinUseCase
import com.mtv.app.shopme.feature.auth.contract.ChangePinEvent
import com.mtv.app.shopme.feature.auth.presentation.ChangePinViewModel
import com.mtv.app.shopme.domain.model.Resource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.IOException
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ChangePinViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()
    private val useCase: ChangePinUseCase = mockk()

    @Test fun `change pin should call backend usecase`() = runTest(dispatcherRule.testDispatcher) {
        every { useCase.invoke(ChangePinParam("111111", "222222")) } returns flowOf(Resource.Success(Unit))
        val vm = ChangePinViewModel(useCase)
        vm.onEvent(ChangePinEvent.OnOldPinChange("111111"))
        vm.onEvent(ChangePinEvent.OnNewPinChange("222222"))
        vm.onEvent(ChangePinEvent.OnConfirmPinChange("222222"))
        vm.onEvent(ChangePinEvent.OnSubmit)
        advanceUntilIdle()
        assertTrue(vm.uiState.value.changePin is com.mtv.based.core.network.utils.LoadState.Success)
    }

    @Test
    fun `change pin should handle error from usecase`() = runTest(dispatcherRule.testDispatcher) {
        every { useCase.invoke(ChangePinParam("111111", "222222")) } returns flowOf(Resource.Error(IOException("API Error")))
        val vm = ChangePinViewModel(useCase)
        vm.onEvent(ChangePinEvent.OnOldPinChange("111111"))
        vm.onEvent(ChangePinEvent.OnNewPinChange("222222"))
        vm.onEvent(ChangePinEvent.OnConfirmPinChange("222222"))
        vm.onEvent(ChangePinEvent.OnSubmit)
        advanceUntilIdle()
        assertTrue(vm.uiState.value.changePin is com.mtv.based.core.network.utils.LoadState.Error)
    }

    @Test
    fun `change pin should show validation error on pin mismatch`() = runTest(dispatcherRule.testDispatcher) {
        val vm = ChangePinViewModel(useCase)
        vm.onEvent(ChangePinEvent.OnOldPinChange("111111"))
        vm.onEvent(ChangePinEvent.OnNewPinChange("222222"))
        vm.onEvent(ChangePinEvent.OnConfirmPinChange("333333"))
        vm.onEvent(ChangePinEvent.OnSubmit)
        advanceUntilIdle()
        assertTrue(vm.uiState.value.changePin is com.mtv.based.core.network.utils.LoadState.Idle)
        verify(exactly = 0) { useCase.invoke(any()) }
    }
}
