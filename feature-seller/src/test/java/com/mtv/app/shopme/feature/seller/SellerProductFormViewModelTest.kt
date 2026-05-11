package com.mtv.app.shopme.feature.seller

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.model.SellerProfile
import com.mtv.app.shopme.domain.param.FoodUpsertParam
import com.mtv.app.shopme.domain.usecase.CreateFoodUseCase
import com.mtv.app.shopme.domain.usecase.DeleteFoodUseCase
import com.mtv.app.shopme.domain.usecase.GetFoodDetailUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.domain.usecase.UpdateFoodUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerProductFormEffect
import com.mtv.app.shopme.feature.seller.contract.SellerProductFormEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerProductFormViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalDateTime
import java.math.BigDecimal

class SellerProductFormViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val sessionManager: SessionManager = mockk(relaxed = true)
    private val getSellerProfileUseCase: GetSellerProfileUseCase = mockk()
    private val getFoodDetailUseCase: GetFoodDetailUseCase = mockk()
    private val createFoodUseCase: CreateFoodUseCase = mockk()
    private val updateFoodUseCase: UpdateFoodUseCase = mockk()
    private val deleteFoodUseCase: DeleteFoodUseCase = mockk()

    @Test
    fun `load edit product should map backend detail to form state`() = runTest {
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
        every { getFoodDetailUseCase.invoke("food-1") } returns flowOf(
            Resource.Success(
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
                    images = listOf("https://cdn.example.com/latte.png"),
                    variants = emptyList()
                )
            )
        )

        val vm = SellerProductFormViewModel(
            sessionManager = sessionManager,
            getSellerProfileUseCase = getSellerProfileUseCase,
            getFoodDetailUseCase = getFoodDetailUseCase,
            createFoodUseCase = createFoodUseCase,
            updateFoodUseCase = updateFoodUseCase,
            deleteFoodUseCase = deleteFoodUseCase,
            savedStateHandle = SavedStateHandle(mapOf("productId" to "food-1"))
        )

        vm.onEvent(SellerProductFormEvent.Load)
        advanceUntilIdle()

        assertEquals("Coffee Latte", vm.uiState.value.product.name)
        assertEquals("25000", vm.uiState.value.product.price)
        assertEquals("https://cdn.example.com/latte.png", vm.uiState.value.images.first())
    }

    @Test
    fun `save new product should call backend and emit success`() = runTest {
        val requestSlot = slot<FoodUpsertParam>()
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
        every { createFoodUseCase.invoke(capture(requestSlot)) } returns flowOf(Resource.Success(Unit))

        val vm = SellerProductFormViewModel(
            sessionManager = sessionManager,
            getSellerProfileUseCase = getSellerProfileUseCase,
            getFoodDetailUseCase = getFoodDetailUseCase,
            createFoodUseCase = createFoodUseCase,
            updateFoodUseCase = updateFoodUseCase,
            deleteFoodUseCase = deleteFoodUseCase,
            savedStateHandle = SavedStateHandle()
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(SellerProductFormEvent.Load)
        advanceUntilIdle()
        vm.onEvent(SellerProductFormEvent.ChangeName("Coffee Latte"))
        vm.onEvent(SellerProductFormEvent.ChangeDescription("Hot coffee"))
        vm.onEvent(SellerProductFormEvent.ChangePrice("25000"))
        vm.onEvent(SellerProductFormEvent.ChangeStock(10))
        vm.onEvent(SellerProductFormEvent.AddImage("data:image/jpeg;base64,abc123"))
        vm.onEvent(SellerProductFormEvent.Save)
        advanceUntilIdle()

        assertEquals(listOf("data:image/jpeg;base64,abc123"), requestSlot.captured.images)
        assertEquals(SellerProductFormEffect.SaveSuccess, effect.await())
    }

    @Test
    fun `help center event should emit navigation effect`() = runTest {
        val vm = SellerProductFormViewModel(
            sessionManager = sessionManager,
            getSellerProfileUseCase = getSellerProfileUseCase,
            getFoodDetailUseCase = getFoodDetailUseCase,
            createFoodUseCase = createFoodUseCase,
            updateFoodUseCase = updateFoodUseCase,
            deleteFoodUseCase = deleteFoodUseCase,
            savedStateHandle = SavedStateHandle()
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(SellerProductFormEvent.ClickHelpCenter)

        assertEquals(SellerProductFormEffect.NavigateToHelpCenter, effect.await())
    }
}
