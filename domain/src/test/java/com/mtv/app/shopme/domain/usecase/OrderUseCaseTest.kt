package com.mtv.app.shopme.domain.usecase

import app.cash.turbine.test
import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.model.PagedData
import com.mtv.app.shopme.domain.repository.OrderRepository
import com.mtv.app.shopme.domain.model.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OrderUseCaseTest {

    private val repository: OrderRepository = mockk()
    private lateinit var getOrdersUseCase: GetOrdersUseCase
    private lateinit var getOrderDetailUseCase: GetOrderDetailUseCase
    private lateinit var cancelOrderUseCase: CancelOrderUseCase
    private lateinit var confirmOrderTransferUseCase: ConfirmOrderTransferUseCase

    @Before
    fun setUp() {
        getOrdersUseCase = GetOrdersUseCase(repository)
        getOrderDetailUseCase = GetOrderDetailUseCase(repository)
        cancelOrderUseCase = CancelOrderUseCase(repository)
        confirmOrderTransferUseCase = ConfirmOrderTransferUseCase(repository)
    }

    @Test
    fun `GetOrdersUseCase invoke delegates to repository getOrders`() = runTest {
        coEvery { repository.getOrders() } returns flowOf(Resource.Success(emptyList()))

        getOrdersUseCase().test {
            assertEquals(Resource.Success(emptyList<Order>()), awaitItem())
            awaitComplete()
        }

        coVerify { repository.getOrders() }
    }

    @Test
    fun `GetOrdersUseCase invoke with page delegates to repository getOrders with paging`() = runTest {
        coEvery { repository.getOrders(1, 20) } returns flowOf(
            Resource.Success(PagedData(content = emptyList(), page = 1, last = false))
        )

        getOrdersUseCase(page = 1, size = 20).test {
            assertEquals(
                Resource.Success(PagedData(content = emptyList<Order>(), page = 1, last = false)),
                awaitItem()
            )
            awaitComplete()
        }

        coVerify { repository.getOrders(1, 20) }
    }

    @Test
    fun `GetOrderDetailUseCase delegates to repository getOrderDetail`() = runTest {
        val orderId = "order-1"
        coEvery { repository.getOrderDetail(orderId) } returns flowOf(Resource.Loading)

        getOrderDetailUseCase(orderId).test {
            assertEquals(Resource.Loading, awaitItem())
            awaitComplete()
        }

        coVerify { repository.getOrderDetail(orderId) }
    }

    @Test
    fun `CancelOrderUseCase delegates to repository cancelOrder with reason`() = runTest {
        val orderId = "order-1"
        val reason = "Changed mind"
        coEvery { repository.cancelOrder(orderId, reason) } returns flowOf(Resource.Success(Unit))

        cancelOrderUseCase(orderId, reason).test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.cancelOrder(orderId, reason) }
    }

    @Test
    fun `CancelOrderUseCase delegates with null reason`() = runTest {
        coEvery { repository.cancelOrder("order-1", null) } returns flowOf(Resource.Success(Unit))

        cancelOrderUseCase("order-1", null).test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.cancelOrder("order-1", null) }
    }

    @Test
    fun `ConfirmOrderTransferUseCase delegates to repository confirmTransfer`() = runTest {
        val orderId = "order-1"
        coEvery { repository.confirmTransfer(orderId) } returns flowOf(Resource.Success(Unit))

        confirmOrderTransferUseCase(orderId).test {
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify { repository.confirmTransfer(orderId) }
    }
}
