package com.mtv.app.shopme.feature.customer

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.model.OrderItem
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.model.PaymentStatus
import com.mtv.app.shopme.domain.usecase.ConfirmOrderTransferUseCase
import com.mtv.app.shopme.domain.usecase.EnsureChatConversationUseCase
import com.mtv.app.shopme.domain.usecase.GetOrderDetailUseCase
import com.mtv.app.shopme.feature.customer.contract.OrderDetailEffect
import com.mtv.app.shopme.feature.customer.contract.OrderDetailEvent
import com.mtv.app.shopme.feature.customer.presentation.OrderDetailViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.provider.utils.SessionManager
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

class OrderDetailViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getOrderDetailUseCase: GetOrderDetailUseCase = mockk()
    private val confirmOrderTransferUseCase: ConfirmOrderTransferUseCase = mockk()
    private val ensureChatConversationUseCase: EnsureChatConversationUseCase = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `load should expose backend order detail`() = runTest {
        every { getOrderDetailUseCase.invoke("order-1") } returns flowOf(
            Resource.Success(
                Order(
                    id = "order-1",
                    cafeId = "cafe-1",
                    cafeName = "Shopme Cafe",
                    customerId = "cust-1",
                    customerName = "Dedy",
                    deliveryAddress = "Kemang Blok A/12",
                    totalPrice = 42000.0,
                    status = OrderStatus.ORDERED,
                    paymentMethod = PaymentMethod.TRANSFER,
                    paymentStatus = PaymentStatus.WAITING_UPLOAD,
                    items = listOf(
                        OrderItem(
                            id = "item-1",
                            foodId = "food-1",
                            foodName = "Es Kopi",
                            quantity = 2,
                            price = 21000.0
                        )
                    )
                )
            )
        )

        val vm = OrderDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("orderId" to "order-1")),
            getOrderDetailUseCase = getOrderDetailUseCase,
            confirmOrderTransferUseCase = confirmOrderTransferUseCase,
            ensureChatConversationUseCase = ensureChatConversationUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(OrderDetailEvent.Load)
        advanceUntilIdle()

        assertEquals("order-1", vm.uiState.value.order?.id)
        assertEquals("Kemang Blok A/12", vm.uiState.value.order?.deliveryAddress)
        assertEquals("Es Kopi", vm.uiState.value.order?.items?.firstOrNull()?.foodName)
    }

    @Test
    fun `confirm transfer should reload latest order detail`() = runTest {
        every { getOrderDetailUseCase.invoke("order-1") } returns flowOf(
            Resource.Success(
                Order(
                    id = "order-1",
                    cafeId = "cafe-1",
                    totalPrice = 25000.0,
                    status = OrderStatus.ORDERED,
                    paymentMethod = PaymentMethod.TRANSFER,
                    paymentStatus = PaymentStatus.WAITING_CONFIRMATION
                )
            )
        )
        every { confirmOrderTransferUseCase.invoke("order-1") } returns flowOf(Resource.Success(Unit))

        val vm = OrderDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("orderId" to "order-1")),
            getOrderDetailUseCase = getOrderDetailUseCase,
            confirmOrderTransferUseCase = confirmOrderTransferUseCase,
            ensureChatConversationUseCase = ensureChatConversationUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(OrderDetailEvent.ConfirmTransfer)
        advanceUntilIdle()

        verify(exactly = 1) { confirmOrderTransferUseCase.invoke("order-1") }
        verify(exactly = 1) { getOrderDetailUseCase.invoke("order-1") }
    }

    @Test
    fun `click chat should ensure scoped conversation from loaded order`() = runTest {
        every { getOrderDetailUseCase.invoke("order-1") } returns flowOf(
            Resource.Success(
                Order(
                    id = "order-1",
                    cafeId = "cafe-1",
                    totalPrice = 12000.0,
                    status = OrderStatus.ORDERED,
                    paymentMethod = PaymentMethod.TRANSFER,
                    paymentStatus = PaymentStatus.WAITING_CONFIRMATION
                )
            )
        )
        every { ensureChatConversationUseCase.invoke("cafe-1") } returns flowOf(Resource.Success("conv-1"))

        val vm = OrderDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("orderId" to "order-1")),
            getOrderDetailUseCase = getOrderDetailUseCase,
            confirmOrderTransferUseCase = confirmOrderTransferUseCase,
            ensureChatConversationUseCase = ensureChatConversationUseCase,
            sessionManager = sessionManager
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(OrderDetailEvent.Load)
        advanceUntilIdle()
        vm.onEvent(OrderDetailEvent.ClickChat)
        advanceUntilIdle()

        assertEquals(OrderDetailEffect.NavigateToChat("conv-1"), effect.await())
    }
}
