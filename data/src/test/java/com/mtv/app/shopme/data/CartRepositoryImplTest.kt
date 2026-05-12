package com.mtv.app.shopme.data

import app.cash.turbine.test
import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.database.entity.PayloadCacheEntity
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.remote.datasource.CartRemoteDataSource
import com.mtv.app.shopme.data.remote.response.CartItemResponse
import com.mtv.app.shopme.data.remote.response.CartItemVariantResponse
import com.mtv.app.shopme.data.repository.CartRepositoryImpl
import com.mtv.app.shopme.data.utils.PayloadCacheStore
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
class CartRepositoryImplTest {

    private val remote: CartRemoteDataSource = mockk()
    private val homeDao: HomeDao = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk(relaxed = true)

    private val repository = CartRepositoryImpl(
        remote = remote,
        resultFlow = ResultFlowFactory(errorMapper),
        homeDao = homeDao,
        errorMapper = errorMapper
    )

    @Test
    fun `getCart should emit cached cart first then refresh from backend`() = runTest {
        val cachedResponse = listOf(cartResponse(id = "cart-1", name = "Cached Latte"))
        val remoteResponse = listOf(cartResponse(id = "cart-2", name = "Remote Latte"))

        coEvery { homeDao.getPayloadOnce("cart:list") } returns PayloadCacheEntity(
            cacheKey = "cart:list",
            payload = PayloadCacheStore.json.encodeToString(
                ListSerializer(CartItemResponse.serializer()),
                cachedResponse
            ),
            updatedAt = 1L
        )
        coEvery { remote.getCart() } returns remoteResponse

        repository.getCart().test {
            assertEquals(Resource.Loading, awaitItem())

            val cached = awaitItem() as Resource.Success
            assertEquals("cart-1", cached.data.first().id)

            val refreshed = awaitItem() as Resource.Success
            assertEquals("cart-2", refreshed.data.first().id)

            awaitComplete()
        }

        coVerify { homeDao.insertPayload(match { it.cacheKey == "cart:list" }) }
    }

    private fun cartResponse(id: String, name: String) = CartItemResponse(
        id = id,
        name = name,
        image = "https://cdn.local/$id.jpg",
        quantity = 1,
        notes = "",
        cafeId = "cafe-1",
        cafeName = "Kopi Senja",
        foodId = "food-1",
        customerId = "customer-1",
        price = BigDecimal(18000),
        variants = listOf(
            CartItemVariantResponse(
                variantId = "variant-1",
                variantName = "Size",
                optionId = "option-1",
                optionName = "Large",
                price = BigDecimal(2000)
            )
        )
    )
}
