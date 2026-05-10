package com.mtv.app.shopme.feature.seller

import com.mtv.app.shopme.domain.model.SellerPaymentMethod
import com.mtv.app.shopme.domain.usecase.GetSellerPaymentMethodsUseCase
import com.mtv.app.shopme.domain.usecase.UpdateSellerPaymentMethodsUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodEffect
import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerPaymentMethodViewModel
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

class SellerPaymentMethodViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getSellerPaymentMethodsUseCase: GetSellerPaymentMethodsUseCase = mockk()
    private val updateSellerPaymentMethodsUseCase: UpdateSellerPaymentMethodsUseCase = mockk()

    @Test
    fun `load should populate seller payment methods from backend`() = runTest {
        every { getSellerPaymentMethodsUseCase.invoke() } returns flowOf(
            Resource.Success(
                SellerPaymentMethod(
                    cashEnabled = true,
                    bankEnabled = true,
                    bankNumber = "1234567890",
                    ovoEnabled = false,
                    ovoNumber = "",
                    danaEnabled = true,
                    danaNumber = "08123",
                    gopayEnabled = false,
                    gopayNumber = ""
                )
            )
        )

        val vm = SellerPaymentMethodViewModel(
            getSellerPaymentMethodsUseCase = getSellerPaymentMethodsUseCase,
            updateSellerPaymentMethodsUseCase = updateSellerPaymentMethodsUseCase
        )

        vm.onEvent(SellerPaymentMethodEvent.Load)
        advanceUntilIdle()

        assertEquals(true, vm.uiState.value.bankEnabled)
        assertEquals("1234567890", vm.uiState.value.bankNumber)
        assertEquals("08123", vm.uiState.value.danaNumber)
    }

    @Test
    fun `save should persist backend payment methods and emit success`() = runTest {
        every { updateSellerPaymentMethodsUseCase.invoke(any()) } returns flowOf(
            Resource.Success(
                SellerPaymentMethod(
                    cashEnabled = true,
                    bankEnabled = true,
                    bankNumber = "1234567890",
                    ovoEnabled = true,
                    ovoNumber = "081111",
                    danaEnabled = false,
                    danaNumber = "",
                    gopayEnabled = false,
                    gopayNumber = ""
                )
            )
        )

        val vm = SellerPaymentMethodViewModel(
            getSellerPaymentMethodsUseCase = getSellerPaymentMethodsUseCase,
            updateSellerPaymentMethodsUseCase = updateSellerPaymentMethodsUseCase
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(SellerPaymentMethodEvent.ToggleCash(true))
        vm.onEvent(SellerPaymentMethodEvent.ToggleBank(true))
        vm.onEvent(SellerPaymentMethodEvent.ChangeBank("1234567890"))
        vm.onEvent(SellerPaymentMethodEvent.ToggleOvo(true))
        vm.onEvent(SellerPaymentMethodEvent.ChangeOvo("081111"))
        vm.onEvent(SellerPaymentMethodEvent.Save)
        advanceUntilIdle()

        assertEquals(SellerPaymentMethodEffect.SaveSuccess, effect.await())
        assertEquals("1234567890", vm.uiState.value.bankNumber)
        assertEquals(true, vm.uiState.value.ovoEnabled)
    }
}
