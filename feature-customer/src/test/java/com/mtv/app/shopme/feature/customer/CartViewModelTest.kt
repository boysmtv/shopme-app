@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.mtv.app.shopme.feature.customer

import com.mtv.app.shopme.domain.model.Address
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.SessionToken
import com.mtv.app.shopme.domain.usecase.CreateOrderUseCase
import com.mtv.app.shopme.domain.usecase.DeleteCartByCafeIdUseCase
import com.mtv.app.shopme.domain.usecase.DeleteCartItemUseCase
import com.mtv.app.shopme.domain.usecase.DeleteCartUseCase
import com.mtv.app.shopme.domain.usecase.GetCartUseCase
import com.mtv.app.shopme.domain.usecase.GetCustomerUseCase
import com.mtv.app.shopme.domain.usecase.GetSessionTokenUseCase
import com.mtv.app.shopme.domain.usecase.GetVerifyPinUseCase
import com.mtv.app.shopme.domain.param.VerifyPinParam
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.feature.customer.contract.CartEffect
import com.mtv.app.shopme.domain.usecase.UpdateCartQuantityUseCase
import com.mtv.app.shopme.feature.customer.contract.CartEvent
import com.mtv.app.shopme.feature.customer.presentation.CartViewModel
import com.mtv.based.core.network.utils.LoadState
import com.mtv.app.shopme.core.error.ApiException
import com.mtv.app.shopme.domain.model.Resource
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CartViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val cartItemUseCase: GetCartUseCase = mockk(relaxed = true)
    private val customerUseCase: GetCustomerUseCase = mockk(relaxed = true)
    private val getSessionTokenUseCase: GetSessionTokenUseCase = mockk(relaxed = true)
    private val verifyPinUseCase: GetVerifyPinUseCase = mockk()
    private val createOrderUseCase: CreateOrderUseCase = mockk(relaxed = true)
    private val cartQuantityUseCase: UpdateCartQuantityUseCase = mockk(relaxed = true)
    private val clearCartByCafeIdUseCase: DeleteCartByCafeIdUseCase = mockk(relaxed = true)
    private val clearCartUseCase: DeleteCartUseCase = mockk(relaxed = true)
    private val deleteCartItemUseCase: DeleteCartItemUseCase = mockk(relaxed = true)
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `checkout clicked should request fresh customer before session token`() = runTest(dispatcherRule.testDispatcher) {
        every { customerUseCase.invoke(forceRefresh = true) } returns flowOf(
            Resource.Success(completeCustomer())
        )
        every { getSessionTokenUseCase.invoke() } returns flowOf(
            Resource.Success(SessionToken("trx-1"))
        )

        val vm = CartViewModel(
            cartItemUseCase = cartItemUseCase,
            customerUseCase = customerUseCase,
            getSessionTokenUseCase = getSessionTokenUseCase,
            verifyPinUseCase = verifyPinUseCase,
            createOrderUseCase = createOrderUseCase,
            cartQuantityUseCase = cartQuantityUseCase,
            clearCartByCafeIdUseCase = clearCartByCafeIdUseCase,
            clearCartUseCase = clearCartUseCase,
            deleteCartItemUseCase = deleteCartItemUseCase,
            sessionManager = sessionManager
        )

        val effect = async { vm.effect.first() }

        vm.onEvent(
            CartEvent.CheckoutClicked(
                cartIds = listOf("cart-1"),
                payment = PaymentMethod.CASH
            )
        )
        advanceUntilIdle()

        assertEquals(CartEffect.OpenPinSheet, effect.await())
        assertTrue(vm.uiState.value.isCheckoutLoading.not())
        io.mockk.verify(exactly = 1) { customerUseCase.invoke(forceRefresh = true) }
        io.mockk.verify(exactly = 1) { getSessionTokenUseCase.invoke() }
    }

    @Test
    fun `invalid pin should not force logout`() = runTest(dispatcherRule.testDispatcher) {
        every { customerUseCase.invoke(forceRefresh = true) } returns flowOf(
            Resource.Success(completeCustomer())
        )
        every { getSessionTokenUseCase.invoke() } returns flowOf(
            Resource.Success(SessionToken("trx-1"))
        )
        every { verifyPinUseCase.invoke(any()) } returns flowOf(
            Resource.Error(throwable = ApiException.Unauthorized(message = "Invalid PIN"))
        )

        val vm = CartViewModel(
            cartItemUseCase = cartItemUseCase,
            customerUseCase = customerUseCase,
            getSessionTokenUseCase = getSessionTokenUseCase,
            verifyPinUseCase = verifyPinUseCase,
            createOrderUseCase = createOrderUseCase,
            cartQuantityUseCase = cartQuantityUseCase,
            clearCartByCafeIdUseCase = clearCartByCafeIdUseCase,
            clearCartUseCase = clearCartUseCase,
            deleteCartItemUseCase = deleteCartItemUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(
            CartEvent.CheckoutClicked(
                cartIds = listOf("cart-1"),
                payment = PaymentMethod.CASH
            )
        )
        advanceUntilIdle()

        vm.onEvent(CartEvent.PinSubmitted(pin = "000000"))
        advanceUntilIdle()

        coVerify(exactly = 1) {
            verifyPinUseCase.invoke(
                VerifyPinParam(
                    token = "trx-1",
                    pin = "000000"
                )
            )
        }
        assertTrue(vm.uiState.value.isCheckoutLoading.not())
        coVerify(exactly = 0) { sessionManager.forceLogout() }
    }

    private fun completeCustomer() = Customer(
        name = "Raka",
        phone = "081234567890",
        email = "raka.pratama@shopme.local",
        address = Address(
            id = "address-1",
            village = "Tebet Timur",
            block = "A",
            number = "12",
            rt = "001",
            rw = "005",
            isDefault = true
        ),
        photo = "",
        verified = true,
        stats = null,
        menuSummary = null
    )
}
