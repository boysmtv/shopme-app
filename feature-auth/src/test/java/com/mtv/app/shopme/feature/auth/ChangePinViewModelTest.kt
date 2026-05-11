package com.mtv.app.shopme.feature.auth

import com.mtv.app.shopme.domain.param.ChangePinParam
import com.mtv.app.shopme.domain.usecase.ChangePinUseCase
import com.mtv.app.shopme.feature.auth.contract.ChangePinEvent
import com.mtv.app.shopme.feature.auth.presentation.ChangePinViewModel
import com.mtv.based.core.network.utils.Resource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ChangePinViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()
    private val useCase: ChangePinUseCase = mockk()

    @Test fun `change pin should call backend usecase`() = runTest {
        every { useCase.invoke(ChangePinParam("111111", "222222")) } returns flowOf(Resource.Success(Unit))
        val vm = ChangePinViewModel(useCase)
        vm.onEvent(ChangePinEvent.OnOldPinChange("111111"))
        vm.onEvent(ChangePinEvent.OnNewPinChange("222222"))
        vm.onEvent(ChangePinEvent.OnConfirmPinChange("222222"))
        vm.onEvent(ChangePinEvent.OnSubmit)
        advanceUntilIdle()
        assertTrue(vm.uiState.value.changePin is com.mtv.based.core.network.utils.LoadState.Success)
    }
}
