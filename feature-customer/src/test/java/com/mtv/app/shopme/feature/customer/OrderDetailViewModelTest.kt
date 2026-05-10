package com.mtv.app.shopme.feature.customer

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.model.OrderItem
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.model.PaymentStatus
import com.mtv.app.shopme.domain.usecase.ConfirmOrderTransferUseCase
import com.mtv.app.shopme.domain.usecase.GetOrderDetailUseCase
import com.mtv.app.shopme.feature.customer.contract.OrderDetailEvent
import com.mtv.app.shopme.feature.customer.presentation.OrderDetailViewModel
import com.mtv.based.core.network.utils.Resource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
            confirmOrderTransferUseCase = confirmOrderTransferUseCase
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
            confirmOrderTransferUseCase = confirmOrderTransferUseCase
        )

        vm.onEvent(OrderDetailEvent.ConfirmTransfer)
        advanceUntilIdle()

        verify(exactly = 1) { confirmOrderTransferUseCase.invoke("order-1") }
        verify(exactly = 1) { getOrderDetailUseCase.invoke("order-1") }
    }
}
