@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.mtv.app.shopme.feature.seller

import app.cash.turbine.test
import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.model.PaymentStatus
import com.mtv.app.shopme.domain.usecase.CancelSellerOrderUseCase
import com.mtv.app.shopme.domain.usecase.EnsureSellerChatConversationUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerOrderDetailUseCase
import com.mtv.app.shopme.domain.usecase.UpdateSellerOrderStatusUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailEffect
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerOrderDetailViewModel
import com.mtv.app.shopme.domain.model.Resource
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SellerOrderDetailViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val detailUseCase: GetSellerOrderDetailUseCase = mockk()
    private val updateUseCase: UpdateSellerOrderStatusUseCase = mockk(relaxed = true)
    private val cancelUseCase: CancelSellerOrderUseCase = mockk(relaxed = true)
    private val ensureSellerChatConversationUseCase: EnsureSellerChatConversationUseCase = mockk(relaxed = true)
    private val sessionManager: com.mtv.based.core.provider.utils.SessionManager = mockk(relaxed = true)

    @Test
    fun `load should expose delivery address from backend order detail`() = runTest(dispatcherRule.testDispatcher) {
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
            ensureSellerChatConversationUseCase = ensureSellerChatConversationUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(SellerOrderDetailEvent.Load)
        advanceUntilIdle()

        assertEquals("Dedy", vm.uiState.value.customerName)
        assertEquals("Kemang Blok A/12, RT 01/RW 02", vm.uiState.value.customerAddress)
    }

    @Test
    fun `load should handle error from order detail usecase`() = runTest(dispatcherRule.testDispatcher) {
        every { detailUseCase.invoke("order-1") } returns flowOf(Resource.Error(IOException("Not found")))

        val vm = SellerOrderDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("orderId" to "order-1")),
            getSellerOrderDetailUseCase = detailUseCase,
            updateSellerOrderStatusUseCase = updateUseCase,
            cancelSellerOrderUseCase = cancelUseCase,
            ensureSellerChatConversationUseCase = ensureSellerChatConversationUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(SellerOrderDetailEvent.Load)
        advanceUntilIdle()

        assertEquals("", vm.uiState.value.customerName)
        assertEquals("", vm.uiState.value.customerAddress)
    }

    @Test
    fun `status update should succeed`() = runTest(dispatcherRule.testDispatcher) {
        every { detailUseCase.invoke("order-1") } returns flowOf(
            Resource.Success(
                Order(
                    id = "order-1",
                    customerId = "cust-1",
                    customerName = "Dedy",
                    cafeId = "cafe-1",
                    cafeName = "Shopme Cafe",
                    deliveryAddress = "Kemang",
                    totalPrice = 25000.0,
                    status = OrderStatus.ORDERED,
                    paymentMethod = PaymentMethod.CASH,
                    paymentStatus = PaymentStatus.UNPAID
                )
            )
        )
        every { updateUseCase.invoke("order-1", OrderStatus.COOKING) } returns flowOf(Resource.Success(Unit))

        val vm = SellerOrderDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("orderId" to "order-1")),
            getSellerOrderDetailUseCase = detailUseCase,
            updateSellerOrderStatusUseCase = updateUseCase,
            cancelSellerOrderUseCase = cancelUseCase,
            ensureSellerChatConversationUseCase = ensureSellerChatConversationUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(SellerOrderDetailEvent.Load)
        advanceUntilIdle()

        vm.onEvent(SellerOrderDetailEvent.ChangeStatus(OrderStatus.COOKING))
        vm.onEvent(SellerOrderDetailEvent.SaveStatus)
        advanceUntilIdle()

        assertEquals(OrderStatus.COOKING, vm.uiState.value.currentStatus)
    }

    @Test
    fun `status update should handle error`() = runTest(dispatcherRule.testDispatcher) {
        every { detailUseCase.invoke("order-1") } returns flowOf(
            Resource.Success(
                Order(
                    id = "order-1",
                    customerId = "cust-1",
                    customerName = "Dedy",
                    cafeId = "cafe-1",
                    cafeName = "Shopme Cafe",
                    deliveryAddress = "Kemang",
                    totalPrice = 25000.0,
                    status = OrderStatus.ORDERED,
                    paymentMethod = PaymentMethod.CASH,
                    paymentStatus = PaymentStatus.UNPAID
                )
            )
        )
        every { updateUseCase.invoke("order-1", OrderStatus.COOKING) } returns flowOf(Resource.Error(IOException("Update failed")))

        val vm = SellerOrderDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("orderId" to "order-1")),
            getSellerOrderDetailUseCase = detailUseCase,
            updateSellerOrderStatusUseCase = updateUseCase,
            cancelSellerOrderUseCase = cancelUseCase,
            ensureSellerChatConversationUseCase = ensureSellerChatConversationUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(SellerOrderDetailEvent.Load)
        advanceUntilIdle()

        vm.onEvent(SellerOrderDetailEvent.ChangeStatus(OrderStatus.COOKING))
        vm.onEvent(SellerOrderDetailEvent.SaveStatus)
        advanceUntilIdle()
    }

    @Test
    fun `cancel order flow should update state`() = runTest(dispatcherRule.testDispatcher) {
        every { detailUseCase.invoke("order-1") } returns flowOf(
            Resource.Success(
                Order(
                    id = "order-1",
                    customerId = "cust-1",
                    customerName = "Dedy",
                    cafeId = "cafe-1",
                    cafeName = "Shopme Cafe",
                    deliveryAddress = "Kemang",
                    totalPrice = 25000.0,
                    status = OrderStatus.ORDERED,
                    paymentMethod = PaymentMethod.CASH,
                    paymentStatus = PaymentStatus.UNPAID
                )
            )
        )
        every { cancelUseCase.invoke("order-1", any()) } returns flowOf(Resource.Success(Unit))

        val vm = SellerOrderDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("orderId" to "order-1")),
            getSellerOrderDetailUseCase = detailUseCase,
            updateSellerOrderStatusUseCase = updateUseCase,
            cancelSellerOrderUseCase = cancelUseCase,
            ensureSellerChatConversationUseCase = ensureSellerChatConversationUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(SellerOrderDetailEvent.Load)
        advanceUntilIdle()

        vm.onEvent(SellerOrderDetailEvent.ClickCancelOrder)
        assertTrue(vm.uiState.value.showCancelDialog)
    }

    @Test
    fun `chat with successful conversation resolution should navigate to chat with conversation id`() = runTest(dispatcherRule.testDispatcher) {
        every { ensureSellerChatConversationUseCase.invoke("order-1") } returns flowOf(Resource.Success("chat-1"))

        val vm = SellerOrderDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("orderId" to "order-1")),
            getSellerOrderDetailUseCase = detailUseCase,
            updateSellerOrderStatusUseCase = updateUseCase,
            cancelSellerOrderUseCase = cancelUseCase,
            ensureSellerChatConversationUseCase = ensureSellerChatConversationUseCase,
            sessionManager = sessionManager
        )

        vm.effect.test {
            vm.onEvent(SellerOrderDetailEvent.ClickChat)
            assertEquals(SellerOrderDetailEffect.NavigateToChat("chat-1"), awaitItem())
        }
    }

    @Test
    fun `chat with failed conversation resolution should show error`() = runTest(dispatcherRule.testDispatcher) {
        every { ensureSellerChatConversationUseCase.invoke("order-1") } returns flowOf(Resource.Error(IOException("Chat failed")))

        val vm = SellerOrderDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("orderId" to "order-1")),
            getSellerOrderDetailUseCase = detailUseCase,
            updateSellerOrderStatusUseCase = updateUseCase,
            cancelSellerOrderUseCase = cancelUseCase,
            ensureSellerChatConversationUseCase = ensureSellerChatConversationUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(SellerOrderDetailEvent.ClickChat)
        advanceUntilIdle()

        assertEquals(false, vm.uiState.value.isLoading)
    }
}
