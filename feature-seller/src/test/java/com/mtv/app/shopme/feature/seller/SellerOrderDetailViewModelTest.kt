package com.mtv.app.shopme.feature.seller

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.model.PaymentStatus
import com.mtv.app.shopme.domain.usecase.CancelSellerOrderUseCase
import com.mtv.app.shopme.domain.usecase.EnsureSellerChatConversationUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerOrderDetailUseCase
import com.mtv.app.shopme.domain.usecase.UpdateSellerOrderStatusUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerOrderDetailViewModel
import com.mtv.based.core.network.utils.Resource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SellerOrderDetailViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val detailUseCase: GetSellerOrderDetailUseCase = mockk()
    private val updateUseCase: UpdateSellerOrderStatusUseCase = mockk(relaxed = true)
    private val cancelUseCase: CancelSellerOrderUseCase = mockk(relaxed = true)
    private val ensureSellerChatConversationUseCase: EnsureSellerChatConversationUseCase = mockk(relaxed = true)

    @Test
    fun `load should expose delivery address from backend order detail`() = runTest {
        every { detailUseCase.invoke("order-1") } returns flowOf(
            Resource.Success(
                Order(
                    id = "order-1",
                    customerId = "cust-1",
                    customerName = "Dedy",
                    cafeId = "cafe-1",
                    cafeName = "Shopme Cafe",
                    deliveryAddress = "Kemang Blok A/12, RT 01/RW 02",
                    totalPrice = 25000.0,
                    status = OrderStatus.ORDERED,
                    paymentMethod = PaymentMethod.CASH,
                    paymentStatus = PaymentStatus.UNPAID
                )
            )
        )

        val vm = SellerOrderDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("orderId" to "order-1")),
            getSellerOrderDetailUseCase = detailUseCase,
            updateSellerOrderStatusUseCase = updateUseCase,
            cancelSellerOrderUseCase = cancelUseCase,
            ensureSellerChatConversationUseCase = ensureSellerChatConversationUseCase
        )

        vm.onEvent(SellerOrderDetailEvent.Load)
        advanceUntilIdle()

        assertEquals("Dedy", vm.uiState.value.customerName)
        assertEquals("Kemang Blok A/12, RT 01/RW 02", vm.uiState.value.customerAddress)
    }
}
