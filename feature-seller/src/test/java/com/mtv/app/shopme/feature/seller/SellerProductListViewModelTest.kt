package com.mtv.app.shopme.feature.seller

import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.model.SellerProfile
import com.mtv.app.shopme.domain.usecase.DeleteFoodUseCase
import com.mtv.app.shopme.domain.usecase.GetFoodsByCafeUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerProductListEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerProductListViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalDateTime
import java.math.BigDecimal

class SellerProductListViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val sessionManager: SessionManager = mockk(relaxed = true)
    private val getSellerProfileUseCase: GetSellerProfileUseCase = mockk()
    private val getFoodsByCafeUseCase: GetFoodsByCafeUseCase = mockk()
    private val deleteFoodUseCase: DeleteFoodUseCase = mockk(relaxed = true)

    @Test
    fun `load should read seller products from backend cafe`() = runTest {
        every { getSellerProfileUseCase.invoke() } returns flowOf(
            Resource.Success(
                SellerProfile(
                    cafeId = "cafe-1",
                    sellerName = "Seller",
                    email = "seller@mail.com",
                    phone = "0812",
                    storeName = "Shopme Cafe",
                    storeAddress = "Kemang",
                    isOnline = true,
                    hasCafe = true
                )
            )
        )
        every { getFoodsByCafeUseCase.invoke("cafe-1") } returns flowOf(
            Resource.Success(
                listOf(
                    Food(
                        id = "food-1",
                        cafeId = "cafe-1",
                        name = "Coffee Latte",
                        cafeName = "Shopme Cafe",
                        cafeAddress = "Kemang",
                        description = "Hot coffee",
                        price = BigDecimal("25000"),
                        category = FoodCategory.DRINK,
                        status = FoodStatus.READY,
                        quantity = 10,
                        estimate = "10 min",
                        isActive = true,
                        createdAt = LocalDateTime.of(2026, 5, 10, 8, 0),
                        images = listOf("data:image/jpeg;base64,coffee"),
                        variants = emptyList()
                    )
                )
            )
        )

        val vm = SellerProductListViewModel(
            sessionManager = sessionManager,
            getSellerProfileUseCase = getSellerProfileUseCase,
            getFoodsByCafeUseCase = getFoodsByCafeUseCase,
            deleteFoodUseCase = deleteFoodUseCase
        )

        vm.onEvent(SellerProductListEvent.Load)
        advanceUntilIdle()

        assertEquals(1, vm.uiState.value.products.size)
        assertEquals("Coffee Latte", vm.uiState.value.products.first().name)
        assertEquals("25000", vm.uiState.value.products.first().price)
        assertEquals("data:image/jpeg;base64,coffee", vm.uiState.value.products.first().image)
    }
}
