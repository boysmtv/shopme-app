@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.mtv.app.shopme.feature.customer

import com.mtv.app.shopme.domain.model.SearchFood
import com.mtv.app.shopme.domain.usecase.AddFavoriteFoodUseCase
import com.mtv.app.shopme.domain.usecase.GetFavoriteFoodIdsUseCase
import com.mtv.app.shopme.domain.usecase.GetFavoriteFoodsUseCase
import com.mtv.app.shopme.domain.usecase.RemoveFavoriteFoodUseCase
import com.mtv.app.shopme.feature.customer.contract.FavoriteEffect
import com.mtv.app.shopme.feature.customer.contract.FavoriteEvent
import com.mtv.app.shopme.feature.customer.presentation.FavoriteViewModel
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class FavoriteViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getFavoriteFoodsUseCase: GetFavoriteFoodsUseCase = mockk()
    private val getFavoriteFoodIdsUseCase: GetFavoriteFoodIdsUseCase = mockk()
    private val addFavoriteFoodUseCase: AddFavoriteFoodUseCase = mockk()
    private val removeFavoriteFoodUseCase: RemoveFavoriteFoodUseCase = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    private val food1 = SearchFood(
        id = "food-1", name = "Ramen", price = BigDecimal("35000"), image = "", cafeName = "Ramen Shop"
    )
    private val food2 = SearchFood(
        id = "food-2", name = "Sushi", price = BigDecimal("45000"), image = "", cafeName = "Sushi Shop"
    )

    @Test
    fun `load should fetch favorite foods and ids`() = runTest {
        every { getFavoriteFoodsUseCase.invoke() } returns flowOf(Resource.Success(listOf<SearchFood>(food1, food2)))
        every { getFavoriteFoodIdsUseCase.invoke() } returns flowOf(Resource.Success(listOf("food-1", "food-2")))

        val vm = FavoriteViewModel(
            getFavoriteFoodsUseCase = getFavoriteFoodsUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(FavoriteEvent.Load)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.foods is LoadState.Success)
        assertEquals(2, (vm.uiState.value.foods as LoadState.Success).data.size)
        assertEquals(2, vm.uiState.value.favoriteIds.size)
    }

    @Test
    fun `toggle favorite should remove food from list when already favorited`() = runTest {
        every { getFavoriteFoodsUseCase.invoke() } returns flowOf(Resource.Success(listOf<SearchFood>(food1, food2)))
        every { getFavoriteFoodIdsUseCase.invoke() } returns flowOf(Resource.Success(listOf("food-1", "food-2")))
        every { removeFavoriteFoodUseCase.invoke("food-1") } returns flowOf(Resource.Success(Unit))

        val vm = FavoriteViewModel(
            getFavoriteFoodsUseCase = getFavoriteFoodsUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(FavoriteEvent.Load)
        advanceUntilIdle()

        vm.onEvent(FavoriteEvent.ToggleFavorite("food-1"))
        advanceUntilIdle()

        assertFalse(vm.uiState.value.favoriteIds.contains("food-1"))
        assertEquals(1, (vm.uiState.value.foods as LoadState.Success).data.size)
        assertEquals("food-2", (vm.uiState.value.foods as LoadState.Success).data.first().id)
    }

    @Test
    fun `click food should emit navigate to detail effect`() = runTest {
        val empty: List<SearchFood> = emptyList()
        every { getFavoriteFoodsUseCase.invoke() } returns flowOf(Resource.Success(empty))
        every { getFavoriteFoodIdsUseCase.invoke() } returns flowOf(Resource.Success(emptyList()))

        val vm = FavoriteViewModel(
            getFavoriteFoodsUseCase = getFavoriteFoodsUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(FavoriteEvent.ClickFood("food-1"))
        advanceUntilIdle()

        assertEquals(FavoriteEffect.NavigateToDetail("food-1"), effect.await())
    }

    @Test
    fun `click back should emit navigate back effect`() = runTest {
        val empty: List<SearchFood> = emptyList()
        every { getFavoriteFoodsUseCase.invoke() } returns flowOf(Resource.Success(empty))
        every { getFavoriteFoodIdsUseCase.invoke() } returns flowOf(Resource.Success(emptyList()))

        val vm = FavoriteViewModel(
            getFavoriteFoodsUseCase = getFavoriteFoodsUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(FavoriteEvent.ClickBack)
        advanceUntilIdle()

        assertEquals(FavoriteEffect.NavigateBack, effect.await())
    }

    @Test
    fun `error loading favorites should handle gracefully`() = runTest {
        val empty: List<SearchFood> = emptyList()
        every { getFavoriteFoodsUseCase.invoke() } returns flowOf(
            Resource.Error(throwable = RuntimeException("Failed"))
        )
        val emptyIds: List<String> = emptyList()
        every { getFavoriteFoodIdsUseCase.invoke() } returns flowOf(Resource.Success(emptyIds))

        val vm = FavoriteViewModel(
            getFavoriteFoodsUseCase = getFavoriteFoodsUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(FavoriteEvent.Load)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.foods is LoadState.Error)
    }

    @Test
    fun `dismiss dialog should clear dialog`() = runTest {
        val empty: List<SearchFood> = emptyList()
        every { getFavoriteFoodsUseCase.invoke() } returns flowOf(Resource.Success(empty))
        every { getFavoriteFoodIdsUseCase.invoke() } returns flowOf(Resource.Success(emptyList()))

        val vm = FavoriteViewModel(
            getFavoriteFoodsUseCase = getFavoriteFoodsUseCase,
            getFavoriteFoodIdsUseCase = getFavoriteFoodIdsUseCase,
            addFavoriteFoodUseCase = addFavoriteFoodUseCase,
            removeFavoriteFoodUseCase = removeFavoriteFoodUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(FavoriteEvent.DismissDialog)

        assertEquals(null, vm.baseUiState.value.dialog)
    }
}
