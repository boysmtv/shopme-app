package com.mtv.app.shopme.feature.customer

import app.cash.turbine.test
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.usecase.GetCustomerUseCase
import com.mtv.app.shopme.domain.usecase.GetFoodUseCase
import com.mtv.app.shopme.feature.customer.contract.HomeEffect
import com.mtv.app.shopme.feature.customer.contract.HomeEvent
import com.mtv.app.shopme.feature.customer.presentation.HomeViewModel
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import java.math.BigDecimal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val customerUseCase: GetCustomerUseCase = mockk()
    private val homeFoodUseCase: GetFoodUseCase = mockk()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(
            customerUseCase = customerUseCase,
            homeFoodUseCase = homeFoodUseCase
        )
    }

    @Test
    fun `should keep successful foods state even if customer request fails`() = runTest {
        val foods = listOf(fakeFood())

        coEvery { customerUseCase() } returns flowOf(Resource.Error(mockk(relaxed = true)))
        coEvery { homeFoodUseCase() } returns flowOf(Resource.Success(foods))

        viewModel.onEvent(HomeEvent.Load)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.foods is LoadState.Success)
    }

    @Test
    fun `should emit navigation effect when click food`() = runTest {
        val id = "food-id"

        viewModel.effect.test {
            viewModel.onEvent(HomeEvent.ClickFood(id))

            assertEquals(HomeEffect.NavigateToDetail(id), awaitItem())
        }
    }
}

private fun fakeCustomer() = Customer(
    name = "John",
    phone = "08123",
    email = "john@mail.com",
    address = null,
    photo = "",
    verified = true,
    stats = null,
    menuSummary = null
)

private fun fakeFood() = Food(
    id = "1",
    cafeId = "c1",
    name = "Coffee",
    cafeName = "Cafe",
    cafeAddress = "Address",
    description = "Good",
    price = BigDecimal(10000),
    category = FoodCategory.DRINK,
    status = FoodStatus.READY,
    quantity = 10,
    estimate = "10 min",
    isActive = true,
    createdAt = LocalDateTime.parse("2026-01-01T10:00:00"),
    images = emptyList(),
    variants = emptyList()
)
