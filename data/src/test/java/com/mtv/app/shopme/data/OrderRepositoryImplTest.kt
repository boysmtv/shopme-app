package com.mtv.app.shopme.data

import app.cash.turbine.test
import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.database.entity.PayloadCacheEntity
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.remote.datasource.OrderRemoteDataSource
import com.mtv.app.shopme.data.remote.response.OrderSummaryItemResponse
import com.mtv.app.shopme.data.remote.response.OrderSummaryResponse
import com.mtv.app.shopme.data.repository.OrderRepositoryImpl
import com.mtv.app.shopme.data.utils.PayloadCacheStore
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.model.PaymentStatus
import com.mtv.based.core.network.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.math.BigDecimal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.builtins.ListSerializer
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OrderRepositoryImplTest {

    private val remote: OrderRemoteDataSource = mockk()
    private val homeDao: HomeDao = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk(relaxed = true)

    private val repository = OrderRepositoryImpl(
        remoteDataSource = remote,
        resultFlow = ResultFlowFactory(errorMapper),
        homeDao = homeDao,
        errorMapper = errorMapper
    )

    @Test
    fun `getOrders should emit cached orders first then refresh from backend`() = runTest {
        val cachedResponse = listOf(orderSummaryResponse(id = "order-1", status = OrderStatus.ORDERED))
        val remoteResponse = listOf(orderSummaryResponse(id = "order-2", status = OrderStatus.DELIVERING))

        coEvery { homeDao.getPayloadOnce("orders:list:customer") } returns PayloadCacheEntity(
            cacheKey = "orders:list:customer",
            payload = PayloadCacheStore.json.encodeToString(
                ListSerializer(OrderSummaryResponse.serializer()),
                cachedResponse
            ),
            updatedAt = 1L
        )
        coEvery { remote.getOrders() } returns remoteResponse

        repository.getOrders().test {
            assertEquals(Resource.Loading, awaitItem())

            val cached = awaitItem() as Resource.Success
            assertEquals("order-1", cached.data.first().id)

            val refreshed = awaitItem() as Resource.Success
            assertEquals("order-2", refreshed.data.first().id)

            awaitComplete()
        }

        coVerify { homeDao.insertPayload(match { it.cacheKey == "orders:list:customer" }) }
    }

    private fun orderSummaryResponse(id: String, status: OrderStatus) = OrderSummaryResponse(
        id = id,
        cafeId = "cafe-1",
        cafeName = "Kopi Senja",
        deliveryAddress = "Jl. Kenanga",
        totalPrice = BigDecimal(42000),
        status = status,
        paymentMethod = PaymentMethod.TRANSFER,
        paymentStatus = PaymentStatus.UNPAID,
        createdAt = "2026-05-12T10:00:00",
        itemCount = 3,
        items = listOf(
            OrderSummaryItemResponse(
                foodName = "Latte",
                quantity = 1
            )
        )
    )
}
