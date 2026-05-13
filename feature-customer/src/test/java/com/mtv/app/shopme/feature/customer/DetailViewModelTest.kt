package com.mtv.app.shopme.feature.customer

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.usecase.AddFavoriteFoodUseCase
import com.mtv.app.shopme.domain.usecase.CreateFoodToCartUseCase
import com.mtv.app.shopme.domain.usecase.EnsureChatConversationUseCase
import com.mtv.app.shopme.domain.usecase.GetCustomerUseCase
import com.mtv.app.shopme.domain.usecase.GetFavoriteFoodIdsUseCase
import com.mtv.app.shopme.domain.usecase.GetFoodDetailUseCase
import com.mtv.app.shopme.domain.usecase.GetFoodSimilarUseCase
import com.mtv.app.shopme.domain.usecase.RemoveFavoriteFoodUseCase
import com.mtv.app.shopme.feature.customer.contract.DetailEffect
import com.mtv.app.shopme.feature.customer.contract.DetailEvent
import com.mtv.app.shopme.feature.customer.presentation.DetailViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.every
import io.mockk.mockk
import java.math.BigDecimal
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalDateTime

class DetailViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val ensureChatConversationUseCase: EnsureChatConversationUseCase = mockk()
    private val customerUseCase: GetCustomerUseCase = mockk(relaxed = true)
    private val foodDetailUseCase: GetFoodDetailUseCase = mockk()
    private val foodSimilarUseCase: GetFoodSimilarUseCase = mockk()
    private val foodAddToCartUseCase: CreateFoodToCartUseCase = mockk()
    private val getFavoriteFoodIdsUseCase: GetFavoriteFoodIdsUseCase = mockk()
    private val addFavoriteFoodUseCase: AddFavoriteFoodUseCase = mockk()
    private val removeFavoriteFoodUseCase: RemoveFavoriteFoodUseCase = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `chat clicked should ensure conversation from loaded cafe`() = runTest {
        every { foodDetailUseCase.invoke("food-1") } returns flowOf(
            Resource.Success(
                Food(
                    id = "food-1",
                    cafeId = "cafe-1",
                    name = "Es Kopi Susu",
                    cafeName = "Shopme Cafe",
                    cafeAddress = "Kemang",
                    description = "Minuman",
                    price = BigDecimal("18000"),
                    category = FoodCategory.DRINK,
                    status = FoodStatus.READY,
                    quantity = 20,
                    estimate = "15 menit",
                    isActive = true,
                    createdAt = LocalDateTime.parse("2026-05-11T10:00:00"),
                    images = emptyList(),
                    variants = emptyList()
                )
            )
        )
        every { foodSimilarUseCase.invoke("cafe-1") } returns flowOf(Resource.Success(emptyList()))
        every { ensureChatConversationUseCase.invoke("cafe-1") } returns flowOf(Resource.Success("conv-1"))
        every { getFavoriteFoodIdsUseCase.invoke() } returns flowOf(Resource.Success(emptyList()))

        val vm = DetailViewModel(
            ensureChatConversationUseCase = ensureChatConversationUseCase,
            customerUseCase = customerUseCase,
            foodDetailUseCase = foodDetailUseCase,
            foodSimilarUseCase = foodSimilarUseCase,
            foodAddToCartUseCase = foodAddToCartUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager,
            savedStateHandle = SavedStateHandle(mapOf("foodId" to "food-1"))
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(DetailEvent.Load)
        advanceUntilIdle()
        vm.onEvent(DetailEvent.ChatClicked)
        advanceUntilIdle()

        assertEquals(DetailEffect.NavigateToChat("conv-1"), effect.await())
    }

    @Test
    fun `toggle favorite should update detail favorite state`() = runTest {
        every { addFavoriteFoodUseCase.invoke("food-1") } returns flowOf(Resource.Success(Unit))

        val vm = DetailViewModel(
            ensureChatConversationUseCase = ensureChatConversationUseCase,
            customerUseCase = customerUseCase,
            foodDetailUseCase = foodDetailUseCase,
            foodSimilarUseCase = foodSimilarUseCase,
            foodAddToCartUseCase = foodAddToCartUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager,
            savedStateHandle = SavedStateHandle(mapOf("foodId" to "food-1"))
        )

        vm.onEvent(DetailEvent.ToggleFavorite)
        advanceUntilIdle()

        assertEquals(true, vm.uiState.value.isFavorite)
    }
}
