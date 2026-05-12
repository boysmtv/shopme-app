package com.mtv.app.shopme.feature.customer

import app.cash.turbine.test
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.PagedData
import com.mtv.app.shopme.domain.model.SearchFood
import com.mtv.app.shopme.domain.param.SearchParam
import com.mtv.app.shopme.domain.usecase.AddFavoriteFoodUseCase
import com.mtv.app.shopme.domain.usecase.GetCustomerUseCase
import com.mtv.app.shopme.domain.usecase.GetFavoriteFoodIdsUseCase
import com.mtv.app.shopme.domain.usecase.GetSearchFoodUseCase
import com.mtv.app.shopme.domain.usecase.RemoveFavoriteFoodUseCase
import com.mtv.app.shopme.feature.customer.contract.HomeEffect
import com.mtv.app.shopme.feature.customer.contract.HomeEvent
import com.mtv.app.shopme.feature.customer.presentation.HomeViewModel
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.provider.utils.SessionManager
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

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val customerUseCase: GetCustomerUseCase = mockk()
    private val homeFoodUseCase: GetSearchFoodUseCase = mockk()
    private val getFavoriteFoodIdsUseCase: GetFavoriteFoodIdsUseCase = mockk()
    private val addFavoriteFoodUseCase: AddFavoriteFoodUseCase = mockk()
    private val removeFavoriteFoodUseCase: RemoveFavoriteFoodUseCase = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(
            customerUseCase = customerUseCase,
            homeFoodUseCase = homeFoodUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager
        )
    }

    @Test
    fun `should keep successful foods state even if customer request fails`() = runTest {
        val foods = listOf(fakeFood())

        coEvery { customerUseCase() } returns flowOf(Resource.Error(mockk(relaxed = true)))
        coEvery { homeFoodUseCase(any()) } returns flowOf(Resource.Success(PagedData(foods, page = 0, last = true)))
        coEvery { getFavoriteFoodIdsUseCase() } returns flowOf(Resource.Success(emptyList()))

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

    @Test
    fun `toggle favorite should update favorite ids`() = runTest {
        coEvery { addFavoriteFoodUseCase("1") } returns flowOf(Resource.Success(Unit))

        viewModel.onEvent(HomeEvent.ToggleFavorite("1"))
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.favoriteIds.contains("1"))
    }

    @Test
    fun `load next page should append foods and update pagination state`() = runTest {
        coEvery { customerUseCase() } returns flowOf(Resource.Success(fakeCustomer()))
        coEvery { getFavoriteFoodIdsUseCase() } returns flowOf(Resource.Success(emptyList()))
        coEvery { homeFoodUseCase(any()) } answers {
            val request = firstArg<SearchParam>()
            when (request.page) {
                0 -> flowOf(
                    Resource.Success(
                        PagedData(
                            content = listOf(fakeFood()),
                            page = 0,
                            last = false
                        )
                    )
                )
                else -> flowOf(
                    Resource.Success(
                        PagedData(
                            content = listOf(
                                SearchFood(
                                    id = "2",
                                    name = "Tea",
                                    price = BigDecimal(12000),
                                    image = "",
                                    cafeName = "Cafe"
                                )
                            ),
                            page = 1,
                            last = true
                        )
                    )
                )
            }
        }

        viewModel.onEvent(HomeEvent.Load)
        advanceUntilIdle()
        viewModel.onEvent(HomeEvent.LoadNextPage)
        advanceUntilIdle()

        val foods = (viewModel.uiState.value.foods as LoadState.Success).data
        assertEquals(2, foods.size)
        assertEquals("1", foods[0].id)
        assertEquals("2", foods[1].id)
        assertEquals(1, viewModel.uiState.value.page)
        assertTrue(viewModel.uiState.value.isLastPage)
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

private fun fakeFood() = SearchFood(
    id = "1",
    name = "Coffee",
    price = BigDecimal(10000),
    image = "",
    cafeName = "Cafe"
)
