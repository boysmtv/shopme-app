package com.mtv.app.shopme.feature.customer

import com.mtv.app.shopme.domain.usecase.CreateOrderUseCase
import com.mtv.app.shopme.domain.usecase.DeleteCartByCafeIdUseCase
import com.mtv.app.shopme.domain.usecase.DeleteCartUseCase
import com.mtv.app.shopme.domain.usecase.GetCartUseCase
import com.mtv.app.shopme.domain.usecase.GetSessionTokenUseCase
import com.mtv.app.shopme.domain.usecase.GetVerifyPinUseCase
import com.mtv.app.shopme.domain.usecase.UpdateCartQuantityUseCase
import com.mtv.app.shopme.feature.customer.contract.CartEvent
import com.mtv.app.shopme.feature.customer.presentation.CartViewModel
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CartViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val cartItemUseCase: GetCartUseCase = mockk(relaxed = true)
    private val getSessionTokenUseCase: GetSessionTokenUseCase = mockk(relaxed = true)
    private val verifyPinUseCase: GetVerifyPinUseCase = mockk()
    private val createOrderUseCase: CreateOrderUseCase = mockk(relaxed = true)
    private val cartQuantityUseCase: UpdateCartQuantityUseCase = mockk(relaxed = true)
    private val clearCartByCafeIdUseCase: DeleteCartByCafeIdUseCase = mockk(relaxed = true)
    private val clearCartUseCase: DeleteCartUseCase = mockk(relaxed = true)
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `invalid pin should not force logout`() = runTest {
        every { verifyPinUseCase.invoke(any()) } returns flowOf(
            Resource.Error(UiError.Unauthorized("Invalid PIN"))
        )

        val vm = CartViewModel(
            cartItemUseCase = cartItemUseCase,
            getSessionTokenUseCase = getSessionTokenUseCase,
            verifyPinUseCase = verifyPinUseCase,
            createOrderUseCase = createOrderUseCase,
            cartQuantityUseCase = cartQuantityUseCase,
            clearCartByCafeIdUseCase = clearCartByCafeIdUseCase,
            clearCartUseCase = clearCartUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(CartEvent.VerifyPin(token = "trx-1", pin = "000000"))
        advanceUntilIdle()

        assertTrue(vm.uiState.value.verifyPin is LoadState.Error)
        coVerify(exactly = 0) { sessionManager.forceLogout() }
    }
}
