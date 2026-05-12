package com.mtv.app.shopme.data

import app.cash.turbine.test
import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.database.entity.CustomerEntity
import com.mtv.app.shopme.core.database.entity.PayloadCacheEntity
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.remote.datasource.CartRemoteDataSource
import com.mtv.app.shopme.data.remote.response.CartItemResponse
import com.mtv.app.shopme.data.remote.response.CartItemVariantResponse
import com.mtv.app.shopme.data.remote.response.FoodOptionResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.remote.response.FoodVariantResponse
import com.mtv.app.shopme.data.repository.CartRepositoryImpl
import com.mtv.app.shopme.data.sync.OfflineMutationSyncManager
import com.mtv.app.shopme.data.utils.PayloadCacheStore
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.param.CartAddParam
import com.mtv.app.shopme.domain.param.CartAddVariantParam
import com.mtv.based.core.network.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.math.BigDecimal
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.builtins.ListSerializer
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class CartRepositoryImplTest {

    private val remote: CartRemoteDataSource = mockk()
    private val homeDao: HomeDao = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk(relaxed = true)
    private val syncManager: OfflineMutationSyncManager = mockk(relaxed = true)

    private val repository = CartRepositoryImpl(
        remote = remote,
        resultFlow = ResultFlowFactory(errorMapper),
        homeDao = homeDao,
        errorMapper = errorMapper,
        syncManager = syncManager
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

    @Test
    fun `addCart should patch local cache when offline and food detail cache exists`() = runTest {
        val currentCart = listOf(cartResponse(id = "cart-1", name = "Cached Latte"))
        val foodDetail = foodResponse()
        coEvery { remote.addCart(any<CartAddParam>()) } throws IOException("offline")
        coEvery { homeDao.getPayloadOnce("cart:list") } returns PayloadCacheEntity(
            cacheKey = "cart:list",
            payload = PayloadCacheStore.json.encodeToString(
                ListSerializer(CartItemResponse.serializer()),
                currentCart
            ),
            updatedAt = 1L
        )
        coEvery { homeDao.getPayloadOnce("food:detail:food-2") } returns PayloadCacheEntity(
            cacheKey = "food:detail:food-2",
            payload = PayloadCacheStore.json.encodeToString(FoodResponse.serializer(), foodDetail),
            updatedAt = 1L
        )
        coEvery { homeDao.getCustomerOnce() } returns customerEntity()

        repository.addCart(
            CartAddParam(
                foodId = "food-2",
                variants = listOf(CartAddVariantParam(variantId = "variant-1", optionId = "option-1")),
                quantity = 2,
                note = ""
            )
        ).test {
            assertEquals(Resource.Loading, awaitItem())
            assertEquals(Resource.Success(Unit), awaitItem())
            awaitComplete()
        }

        coVerify {
            homeDao.insertPayload(match {
                it.cacheKey == "cart:list" &&
                    PayloadCacheStore.json.decodeFromString(
                        ListSerializer(CartItemResponse.serializer()),
                        it.payload
                    ).any { item ->
                        item.foodId == "food-2" &&
                            item.cafeId == "cafe-2" &&
                            item.quantity == 2
                    }
            })
        }
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

    private fun foodResponse() = FoodResponse(
        id = "food-2",
        cafeId = "cafe-2",
        name = "Spicy Burger",
        cafeName = "Burger Senja",
        cafeAddress = "Jalan Merdeka 12",
        description = "Test item",
        price = BigDecimal(25000),
        category = FoodCategory.FOOD,
        status = FoodStatus.READY,
        quantity = 10,
        estimate = "15 min",
        isActive = true,
        createdAt = LocalDateTime.parse("2026-05-12T16:00:00"),
        images = listOf("https://cdn.local/food-2.jpg"),
        variants = listOf(
            FoodVariantResponse(
                id = "variant-1",
                name = "Size",
                options = listOf(
                    FoodOptionResponse(
                        id = "option-1",
                        name = "Large",
                        price = BigDecimal(3000)
                    )
                )
            )
        )
    )

    private fun customerEntity() = CustomerEntity(
        id = "customer-1",
        name = "Raka",
        phone = "08123",
        email = "raka@shopme.local",
        addressVillage = "",
        addressBlock = "",
        addressNumber = "",
        addressRt = "",
        addressRw = "",
        photo = "",
        verified = true,
        totalOrders = 0,
        activeOrders = 0,
        membership = "",
        ordered = 0,
        cooking = 0,
        shipping = 0,
        completed = 0,
        cancelled = 0,
        updatedAt = 1L
    )
}
