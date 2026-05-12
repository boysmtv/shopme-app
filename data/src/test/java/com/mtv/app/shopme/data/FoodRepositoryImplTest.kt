package com.mtv.app.shopme.data

import app.cash.turbine.test
import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.database.entity.FoodEntity
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.data.remote.datasource.FoodRemoteDataSource
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.remote.response.PageResponse
import com.mtv.app.shopme.data.repository.FoodRepositoryImpl
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.param.SearchParam
import com.mtv.based.core.network.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.math.BigDecimal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.threeten.bp.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class FoodRepositoryImplTest {

    private val remote: FoodRemoteDataSource = mockk()
    private val homeDao: HomeDao = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk(relaxed = true)

    private val repository = FoodRepositoryImpl(
        remote = remote,
        resultFlow = mockk(relaxed = true),
        homeDao = homeDao,
        errorMapper = errorMapper
    )

    @Test
    fun `getFoods should emit cached foods first then refresh from backend`() = runTest {
        coEvery { homeDao.getFoodsOnce() } returns listOf(
            FoodEntity(
                id = "cached-1",
                name = "Cached Coffee",
                price = 18000.0,
                image = "cached-image",
                cafeName = "Cached Cafe",
                isActive = true
            )
        )
        coEvery { remote.getFoods() } returns listOf(
            foodResponse(
                id = "remote-1",
                name = "Remote Coffee",
                cafeName = "Remote Cafe"
            )
        )

        repository.getFoods().test {
            assertEquals(Resource.Loading, awaitItem())

            val cached = awaitItem() as Resource.Success
            assertEquals("cached-1", cached.data.first().id)

            val refreshed = awaitItem() as Resource.Success
            assertEquals("remote-1", refreshed.data.first().id)

            awaitComplete()
        }

        coVerify { homeDao.clearFoods() }
        coVerify { homeDao.insertFoods(match { it.single().id == "remote-1" }) }
    }

    @Test
    fun `searchFoods should emit cached home feed first then refresh from backend`() = runTest {
        coEvery { homeDao.getFoodsOnce() } returns listOf(
            FoodEntity(
                id = "cached-1",
                name = "Cached Coffee",
                price = 18000.0,
                image = "cached-image",
                cafeName = "Cached Cafe",
                isActive = true
            )
        )
        coEvery { remote.searchFoods(SearchParam(name = "", page = 0)) } returns PageResponse(
            content = listOf(
                foodResponse(
                    id = "remote-1",
                    name = "Remote Coffee",
                    cafeName = "Remote Cafe"
                )
            ),
            page = 0,
            size = 10,
            totalElements = 1,
            totalPages = 1,
            last = true
        )

        repository.searchFoods(SearchParam(name = "", page = 0)).test {
            assertEquals(Resource.Loading, awaitItem())

            val cached = awaitItem() as Resource.Success
            assertEquals("cached-1", cached.data.content.first().id)

            val refreshed = awaitItem() as Resource.Success
            assertEquals("remote-1", refreshed.data.content.first().id)
            assertTrue(refreshed.data.last)

            awaitComplete()
        }

        coVerify { homeDao.clearFoods() }
        coVerify { homeDao.insertFoods(match { it.single().id == "remote-1" }) }
    }

    @Test
    fun `searchFoods should keep cached home feed when backend refresh fails`() = runTest {
        coEvery { homeDao.getFoodsOnce() } returns listOf(
            FoodEntity(
                id = "cached-1",
                name = "Cached Coffee",
                price = 18000.0,
                image = "cached-image",
                cafeName = "Cached Cafe",
                isActive = true
            )
        )
        coEvery { remote.searchFoods(SearchParam(name = "", page = 0)) } throws IllegalStateException("backend down")

        repository.searchFoods(SearchParam(name = "", page = 0)).test {
            assertEquals(Resource.Loading, awaitItem())

            val cached = awaitItem() as Resource.Success
            assertEquals("cached-1", cached.data.content.first().id)

            awaitComplete()
        }
    }

    private fun foodResponse(
        id: String,
        name: String,
        cafeName: String
    ) = FoodResponse(
        id = id,
        cafeId = "cafe-1",
        name = name,
        cafeName = cafeName,
        cafeAddress = "Jakarta",
        description = "desc",
        price = BigDecimal(18000),
        category = FoodCategory.FOOD,
        status = FoodStatus.READY,
        quantity = 10,
        estimate = "10 min",
        isActive = true,
        createdAt = LocalDateTime.parse("2026-05-12T00:00:00"),
        images = listOf("image"),
        variants = emptyList()
    )
}
