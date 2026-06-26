@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.mtv.app.shopme.feature.customer

import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.model.OrderItem
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.usecase.GetOrderHistoryUseCase
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryEffect
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryEvent
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryItem
import com.mtv.app.shopme.feature.customer.contract.OrderFilter
import com.mtv.app.shopme.feature.customer.presentation.OrderHistoryViewModel
import com.mtv.app.shopme.domain.model.Resource
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class OrderHistoryViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getOrdersUseCase: GetOrderHistoryUseCase = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    private val sampleOrder = Order(
        id = "order-1",
        cafeName = "Kopi Kita",
        cafeId = "cafe-1",
        items = listOf(
            OrderItem(id = "item-1", foodName = "Espresso", quantity = 2, price = 25000.0)
        ),
        totalPrice = 50000.0,
        status = OrderStatus.COMPLETED,
        paymentMethod = PaymentMethod.TRANSFER,
        createdAt = "2026-06-01T10:00:00",
        itemCount = 2
    )

    @Test
    fun `load should populate orders and clear loading`() = runTest {
        every { getOrdersUseCase.invoke() } returns flowOf(Resource.Success(listOf(sampleOrder)))
        val vm = OrderHistoryViewModel(getOrdersUseCase = getOrdersUseCase, sessionManager = sessionManager)

        vm.onEvent(OrderHistoryEvent.Load)
        advanceUntilIdle()

        assertFalse(vm.uiState.value.loading)
        assertEquals(1, vm.uiState.value.orders.size)
        assertEquals("Kopi Kita", vm.uiState.value.orders.first().storeName)
    }

    @Test
    fun `click order should emit navigate to detail effect`() = runTest {
        every { getOrdersUseCase.invoke() } returns flowOf(Resource.Success(listOf(sampleOrder)))
        val vm = OrderHistoryViewModel(getOrdersUseCase = getOrdersUseCase, sessionManager = sessionManager)
        val effect = async { vm.effect.first() }

        vm.onEvent(OrderHistoryEvent.Load)
        advanceUntilIdle()

        val item = vm.uiState.value.orders.first()
        vm.onEvent(OrderHistoryEvent.ClickOrder(item))
        advanceUntilIdle()

        assertEquals(OrderHistoryEffect.NavigateToDetail(item), effect.await())
    }

    @Test
    fun `change filter should update selected filter`() = runTest {
        val vm = OrderHistoryViewModel(getOrdersUseCase = getOrdersUseCase, sessionManager = sessionManager)

        vm.onEvent(OrderHistoryEvent.ChangeFilter(OrderFilter.COMPLETED))

        assertEquals(OrderFilter.COMPLETED, vm.uiState.value.selectedFilter)
    }

    @Test
    fun `click back should emit navigate back effect`() = runTest {
        val vm = OrderHistoryViewModel(getOrdersUseCase = getOrdersUseCase, sessionManager = sessionManager)
        val effect = async { vm.effect.first() }

        vm.onEvent(OrderHistoryEvent.ClickBack)
        advanceUntilIdle()

        assertEquals(OrderHistoryEffect.NavigateBack, effect.await())
    }

    @Test
    fun `refresh should reload orders`() = runTest {
        every { getOrdersUseCase.invoke() } returns flowOf(Resource.Success(listOf(sampleOrder)))
        val vm = OrderHistoryViewModel(getOrdersUseCase = getOrdersUseCase, sessionManager = sessionManager)

        vm.onEvent(OrderHistoryEvent.Refresh)
        advanceUntilIdle()

        assertFalse(vm.uiState.value.loading)
        assertEquals(1, vm.uiState.value.orders.size)
    }

    @Test
    fun `error state should be handled gracefully`() = runTest {
        every { getOrdersUseCase.invoke() } returns flowOf(
            Resource.Error(throwable = RuntimeException("Failed to load"))
        )
        val vm = OrderHistoryViewModel(getOrdersUseCase = getOrdersUseCase, sessionManager = sessionManager)

        vm.onEvent(OrderHistoryEvent.Load)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.orders.isEmpty())
    }
}
