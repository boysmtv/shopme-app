package com.mtv.app.shopme.feature.customer

import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.model.PaymentStatus
import com.mtv.app.shopme.domain.usecase.ConfirmOrderTransferUseCase
import com.mtv.app.shopme.domain.usecase.EnsureChatConversationUseCase
import com.mtv.app.shopme.domain.usecase.GetOrdersUseCase
import com.mtv.app.shopme.feature.customer.contract.OrderEffect
import com.mtv.app.shopme.feature.customer.contract.OrderEvent
import com.mtv.app.shopme.feature.customer.presentation.OrderViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class OrderViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getOrdersUseCase: GetOrdersUseCase = mockk()
    private val confirmOrderTransferUseCase: ConfirmOrderTransferUseCase = mockk()
    private val ensureChatConversationUseCase: EnsureChatConversationUseCase = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `confirm transfer should call backend flow and reload orders`() = runTest {
        val orders = listOf(
            Order(
                id = "order-1",
                cafeId = "cafe-1",
                cafeName = "Kopi Kita",
                totalPrice = 25000.0,
                status = OrderStatus.ORDERED,
                paymentMethod = PaymentMethod.TRANSFER,
                paymentStatus = PaymentStatus.WAITING_UPLOAD
            )
        )
        every { getOrdersUseCase.invoke() } returns flowOf(Resource.Success(orders))
        every { confirmOrderTransferUseCase.invoke("order-1") } returns flowOf(Resource.Success(Unit))

        val vm = OrderViewModel(
            getOrdersUseCase = getOrdersUseCase,
            confirmOrderTransferUseCase = confirmOrderTransferUseCase,
            ensureChatConversationUseCase = ensureChatConversationUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(OrderEvent.Load)
        advanceUntilIdle()
        vm.onEvent(OrderEvent.ConfirmTransfer("order-1"))
        advanceUntilIdle()

        assertEquals(1, vm.uiState.value.orders.size)
        verify(atLeast = 2) { getOrdersUseCase.invoke() }
        verify(exactly = 1) { confirmOrderTransferUseCase.invoke("order-1") }
    }

    @Test
    fun `forbidden order error should not force logout`() = runTest {
        every { getOrdersUseCase.invoke() } returns flowOf(
            Resource.Error(UiError.Forbidden("Access denied"))
        )

        val vm = OrderViewModel(
            getOrdersUseCase = getOrdersUseCase,
            confirmOrderTransferUseCase = confirmOrderTransferUseCase,
            ensureChatConversationUseCase = ensureChatConversationUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(OrderEvent.Load)
        advanceUntilIdle()

        assertEquals(false, vm.uiState.value.isLoading)
        coVerify(exactly = 0) { sessionManager.forceLogout() }
    }

    @Test
    fun `click chat should ensure conversation for selected cafe`() = runTest {
        every { ensureChatConversationUseCase.invoke("cafe-1") } returns flowOf(Resource.Success("conv-1"))

        val vm = OrderViewModel(
            getOrdersUseCase = getOrdersUseCase,
            confirmOrderTransferUseCase = confirmOrderTransferUseCase,
            ensureChatConversationUseCase = ensureChatConversationUseCase,
            sessionManager = sessionManager
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(OrderEvent.ClickChat("cafe-1"))
        advanceUntilIdle()

        assertEquals(OrderEffect.NavigateToChat("conv-1"), effect.await())
    }
}
