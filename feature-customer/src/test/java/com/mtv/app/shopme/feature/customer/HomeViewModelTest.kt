package com.mtv.app.shopme.feature.customer

import app.cash.turbine.test
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.usecase.CustomerUseCase
import com.mtv.app.shopme.domain.usecase.HomeFoodUseCase
import com.mtv.app.shopme.feature.customer.contract.HomeEffect
import com.mtv.app.shopme.feature.customer.contract.HomeEvent
import com.mtv.app.shopme.feature.customer.presentation.HomeViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SecurePrefs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.math.BigDecimal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val securePref: SecurePrefs = mockk(relaxed = true)
    private val customerUseCase: CustomerUseCase = mockk()
    private val homeFoodUseCase: HomeFoodUseCase = mockk()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(
            securePref,
            customerUseCase,
            homeFoodUseCase
        )
    }

    @Test
    fun `should emit success when load success`() = runTest {
        val customer = fakeCustomer()
        val foods = listOf(fakeFood())

        coEvery { customerUseCase() } returns flowOf(Resource.Success(customer))
        coEvery { homeFoodUseCase() } returns flowOf(Resource.Success(foods))

        viewModel.uiState.test {
            viewModel.onEvent(HomeEvent.Load)

            skipItems(1) // skip initial state

            val state = awaitItem()

            assert(state.customer == customer)
            assert(state.foods == foods)
            assert(state.error == null)
        }

        coVerify(exactly = 1) {
            securePref.putObject(any(), customer)
        }
    }

    @Test
    fun `should emit error when customer fails`() = runTest {
        val error = UiError.Unknown("error")

        coEvery { customerUseCase() } returns flowOf(Resource.Error(error))
        coEvery { homeFoodUseCase() } returns flowOf(Resource.Success(emptyList()))

        viewModel.uiState.test {
            viewModel.onEvent(HomeEvent.Load)

            skipItems(1)

            val state = awaitItem()

            assert(state.error == error)
        }
    }

    @Test
    fun `should emit foods even if customer success`() = runTest {
        val foods = listOf(fakeFood())

        coEvery { customerUseCase() } returns flowOf(Resource.Success(fakeCustomer()))
        coEvery { homeFoodUseCase() } returns flowOf(Resource.Success(foods))

        viewModel.uiState.test {
            viewModel.onEvent(HomeEvent.Load)

            skipItems(1)

            val state = awaitItem()

            assert(state.foods == foods)
        }
    }

    @Test
    fun `should emit navigation effect when click food`() = runTest {
        val id = "food-id"

        viewModel.effect.test {
            viewModel.onEvent(HomeEvent.ClickFood(id))

            val effect = awaitItem()

            assert(effect == HomeEffect.NavigateToDetail(id))
        }
    }

    @Test
    fun `should clear error when dismiss`() = runTest {
        val error = UiError.Unknown("error")

        coEvery { customerUseCase() } returns flowOf(Resource.Error(error))
        coEvery { homeFoodUseCase() } returns flowOf(Resource.Success(emptyList()))

        viewModel.onEvent(HomeEvent.Load)

        viewModel.uiState.test {
            skipItems(1)

            val errorState = awaitItem()
            assert(errorState.error != null)

            viewModel.onEvent(HomeEvent.DismissDialog)

            val cleared = awaitItem()
            assert(cleared.error == null)
        }
    }
}

fun fakeCustomer() = Customer(
    name = "John",
    phone = "08123",
    email = "john@mail.com",
    address = null,
    photo = "",
    verified = true,
    stats = null,
    menuSummary = null
)

fun fakeFood() = Food(
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
    createdAt = LocalDateTime.now(),
    images = emptyList(),
    variants = emptyList()
)