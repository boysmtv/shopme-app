package com.mtv.app.shopme.data

import app.cash.turbine.test
import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.database.entity.PayloadCacheEntity
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.remote.datasource.CafeRemoteDataSource
import com.mtv.app.shopme.data.remote.response.CafeAddressResponse
import com.mtv.app.shopme.data.remote.response.CafeResponse
import com.mtv.app.shopme.data.repository.CafeRepositoryImpl
import com.mtv.app.shopme.data.utils.PayloadCacheStore
import com.mtv.based.core.network.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.math.BigDecimal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CafeRepositoryImplTest {

    private val remote: CafeRemoteDataSource = mockk()
    private val homeDao: HomeDao = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk(relaxed = true)

    private val repository = CafeRepositoryImpl(
        remote = remote,
        resultFlow = ResultFlowFactory(errorMapper),
        homeDao = homeDao,
        errorMapper = errorMapper
    )

    @Test
    fun `getCafe should emit cached cafe first then refresh from backend`() = runTest {
        val cached = cafeResponse(id = "cafe-1", name = "Cached Cafe")
        val remoteResponse = cafeResponse(id = "cafe-1", name = "Remote Cafe")

        coEvery { homeDao.getPayloadOnce("cafe:detail:cafe-1") } returns PayloadCacheEntity(
            cacheKey = "cafe:detail:cafe-1",
            payload = PayloadCacheStore.json.encodeToString(CafeResponse.serializer(), cached),
            updatedAt = 1L
        )
        coEvery { remote.getCafe("cafe-1") } returns remoteResponse

        repository.getCafe("cafe-1").test {
            assertEquals(Resource.Loading, awaitItem())

            val cachedItem = awaitItem() as Resource.Success
            assertEquals("Cached Cafe", cachedItem.data.name)

            val refreshed = awaitItem() as Resource.Success
            assertEquals("Remote Cafe", refreshed.data.name)

            awaitComplete()
        }

        coVerify { homeDao.insertPayload(match { it.cacheKey == "cafe:detail:cafe-1" }) }
    }

    private fun cafeResponse(id: String, name: String) = CafeResponse(
        id = id,
        name = name,
        phone = "08123",
        description = "Cafe desc",
        minimalOrder = BigDecimal(15000),
        openTime = "08:00",
        closeTime = "22:00",
        image = "https://cdn.local/$id.jpg",
        isActive = true,
        address = CafeAddressResponse(
            id = "addr-1",
            name = "Melati",
            block = "A",
            number = "7",
            rt = "01",
            rw = "02"
        )
    )
}
