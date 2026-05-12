package com.mtv.app.shopme.data

import app.cash.turbine.test
import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.database.entity.PayloadCacheEntity
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.remote.datasource.SellerRemoteDataSource
import com.mtv.app.shopme.data.remote.response.SellerProfileResponse
import com.mtv.app.shopme.data.repository.SellerRepositoryImpl
import com.mtv.app.shopme.data.sync.OfflineMutationSyncManager
import com.mtv.app.shopme.data.utils.PayloadCacheStore
import com.mtv.based.core.network.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SellerRepositoryImplTest {

    private val remote: SellerRemoteDataSource = mockk()
    private val homeDao: HomeDao = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk(relaxed = true)
    private val syncManager: OfflineMutationSyncManager = mockk(relaxed = true)

    private val repository = SellerRepositoryImpl(
        remote = remote,
        resultFlow = ResultFlowFactory(errorMapper),
        homeDao = homeDao,
        errorMapper = errorMapper,
        syncManager = syncManager
    )

    @Test
    fun `getProfile should emit cached seller profile first then refresh from backend`() = runTest {
        val cached = sellerProfile(storeName = "Cached Store")
        val remoteResponse = sellerProfile(storeName = "Remote Store")

        coEvery { homeDao.getPayloadOnce("seller:profile") } returns PayloadCacheEntity(
            cacheKey = "seller:profile",
            payload = PayloadCacheStore.json.encodeToString(SellerProfileResponse.serializer(), cached),
            updatedAt = 1L
        )
        coEvery { remote.getProfile() } returns remoteResponse

        repository.getProfile().test {
            assertEquals(Resource.Loading, awaitItem())

            val cachedItem = awaitItem() as Resource.Success
            assertEquals("Cached Store", cachedItem.data.storeName)

            val refreshed = awaitItem() as Resource.Success
            assertEquals("Remote Store", refreshed.data.storeName)

            awaitComplete()
        }

        coVerify { homeDao.insertPayload(match { it.cacheKey == "seller:profile" }) }
    }

    private fun sellerProfile(storeName: String) = SellerProfileResponse(
        cafeId = "cafe-1",
        sellerName = "Ayu",
        sellerPhoto = "https://cdn.local/seller.jpg",
        email = "ayu@shopme.local",
        phone = "08123",
        storeName = storeName,
        storePhoto = "https://cdn.local/store.jpg",
        storeAddress = "Jl. Kenanga",
        isOnline = true,
        hasCafe = true
    )
}
