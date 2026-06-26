@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.mtv.app.shopme.feature.customer

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.domain.model.PagedData
import com.mtv.app.shopme.domain.model.SearchFood
import com.mtv.app.shopme.domain.param.DiscoveryParam
import com.mtv.app.shopme.domain.param.SearchParam
import com.mtv.app.shopme.domain.usecase.AddFavoriteFoodUseCase
import com.mtv.app.shopme.domain.usecase.GetDiscoveryFoodUseCase
import com.mtv.app.shopme.domain.usecase.GetFavoriteFoodIdsUseCase
import com.mtv.app.shopme.domain.usecase.GetSearchFoodUseCase
import com.mtv.app.shopme.domain.usecase.RemoveFavoriteFoodUseCase
import com.mtv.app.shopme.feature.customer.contract.SearchEffect
import com.mtv.app.shopme.feature.customer.contract.SearchEvent
import com.mtv.app.shopme.feature.customer.presentation.SearchViewModel
import com.mtv.app.shopme.domain.model.Resource
import com.mtv.based.core.network.utils.LoadState
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
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val searchFoodUseCase: GetSearchFoodUseCase = mockk()
    private val discoveryFoodUseCase: GetDiscoveryFoodUseCase = mockk()
    private val getFavoriteFoodIdsUseCase: GetFavoriteFoodIdsUseCase = mockk()
    private val addFavoriteFoodUseCase: AddFavoriteFoodUseCase = mockk()
    private val removeFavoriteFoodUseCase: RemoveFavoriteFoodUseCase = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `load with blank query should fetch discovery foods`() = runTest {
        every { discoveryFoodUseCase.invoke(any<DiscoveryParam>()) } returns flowOf(
            Resource.Success(PagedData(listOf(foodItem("food-1", "Ramen")), 0, true))
        )
        every { getFavoriteFoodIdsUseCase.invoke() } returns flowOf(Resource.Success(emptyList()))

        val vm = SearchViewModel(
            savedStateHandle = SavedStateHandle(),
            searchFoodUseCase = searchFoodUseCase,
            discoveryFoodUseCase = discoveryFoodUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(SearchEvent.Load)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.foods is LoadState.Success)
        assertEquals(1, (vm.uiState.value.foods as LoadState.Success).data.size)
    }

    @Test
    fun `query changed triggers debounced search after delay`() = runTest {
        every { searchFoodUseCase.invoke(any<SearchParam>()) } returns flowOf(
            Resource.Success(PagedData(listOf(foodItem("food-2", "Mie Ayam")), 0, true))
        )
        every { getFavoriteFoodIdsUseCase.invoke() } returns flowOf(Resource.Success(emptyList()))

        val vm = SearchViewModel(
            savedStateHandle = SavedStateHandle(),
            searchFoodUseCase = searchFoodUseCase,
            discoveryFoodUseCase = discoveryFoodUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(SearchEvent.QueryChanged("Mie"))
        advanceUntilIdle()

        assertEquals("Mie", vm.uiState.value.query)
    }

    @Test
    fun `back clicked should emit navigate back effect`() = runTest {
        every { getFavoriteFoodIdsUseCase.invoke() } returns flowOf(Resource.Success(emptyList()))

        val vm = SearchViewModel(
            savedStateHandle = SavedStateHandle(),
            searchFoodUseCase = searchFoodUseCase,
            discoveryFoodUseCase = discoveryFoodUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(SearchEvent.BackClicked)
        advanceUntilIdle()

        assertEquals(SearchEffect.NavigateBack, effect.await())
    }

    @Test
    fun `click food should emit navigate to detail effect`() = runTest {
        every { getFavoriteFoodIdsUseCase.invoke() } returns flowOf(Resource.Success(emptyList()))

        val vm = SearchViewModel(
            savedStateHandle = SavedStateHandle(),
            searchFoodUseCase = searchFoodUseCase,
            discoveryFoodUseCase = discoveryFoodUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(SearchEvent.ClickFood("food-1"))
        advanceUntilIdle()

        assertEquals(SearchEffect.NavigateToDetail("food-1"), effect.await())
    }

    @Test
    fun `click favorites should emit navigate to favorites effect`() = runTest {
        every { getFavoriteFoodIdsUseCase.invoke() } returns flowOf(Resource.Success(emptyList()))

        val vm = SearchViewModel(
            savedStateHandle = SavedStateHandle(),
            searchFoodUseCase = searchFoodUseCase,
            discoveryFoodUseCase = discoveryFoodUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(SearchEvent.ClickFavorites)
        advanceUntilIdle()

        assertEquals(SearchEffect.NavigateToFavorites, effect.await())
    }

    @Test
    fun `toggle favorite should add food id when not yet favorited`() = runTest {
        every { getFavoriteFoodIdsUseCase.invoke() } returns flowOf(Resource.Success(emptyList()))
        every { addFavoriteFoodUseCase.invoke("food-1") } returns flowOf(Resource.Success(Unit))

        val vm = SearchViewModel(
            savedStateHandle = SavedStateHandle(),
            searchFoodUseCase = searchFoodUseCase,
            discoveryFoodUseCase = discoveryFoodUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(SearchEvent.ToggleFavorite("food-1"))
        advanceUntilIdle()

        assertTrue(vm.uiState.value.favoriteIds.contains("food-1"))
    }

    @Test
    fun `toggle favorite should remove food id when already favorited`() = runTest {
        every { getFavoriteFoodIdsUseCase.invoke() } returns flowOf(Resource.Success(listOf("food-1")))
        every { removeFavoriteFoodUseCase.invoke("food-1") } returns flowOf(Resource.Success(Unit))

        val vm = SearchViewModel(
            savedStateHandle = SavedStateHandle(),
            searchFoodUseCase = searchFoodUseCase,
            discoveryFoodUseCase = discoveryFoodUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(SearchEvent.ToggleFavorite("food-1"))
        advanceUntilIdle()

        assertTrue(vm.uiState.value.favoriteIds.isEmpty())
    }

    private fun foodItem(id: String, name: String) = SearchFood(
        id = id,
        name = name,
        price = BigDecimal("25000"),
        image = "",
        cafeName = "Kafe"
    )
}
