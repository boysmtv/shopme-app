package com.mtv.app.shopme.data

import app.cash.turbine.test
import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.database.entity.CustomerEntity
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.remote.datasource.ProfileRemoteDataSource
import com.mtv.app.shopme.data.remote.response.AddressResponse
import com.mtv.app.shopme.data.remote.response.CustomerResponse
import com.mtv.app.shopme.data.remote.response.MenuSummaryResponse
import com.mtv.app.shopme.data.remote.response.StatsResponse
import com.mtv.app.shopme.data.repository.ProfileRepositoryImpl
import com.mtv.app.shopme.data.sync.OfflineMutationSyncManager
import com.mtv.app.shopme.domain.param.CustomerUpdateParam
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.UiError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileRepositoryImplTest {

    private val remote: ProfileRemoteDataSource = mockk()
    private val homeDao: HomeDao = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk()
    private val syncManager: OfflineMutationSyncManager = mockk(relaxed = true)

    private val repository = ProfileRepositoryImpl(
        remote = remote,
        resultFlow = ResultFlowFactory(errorMapper),
        homeDao = homeDao,
        errorMapper = errorMapper,
        syncManager = syncManager
    )

    @Test
    fun `getCustomer should emit cached customer first then refresh from backend`() = runTest {
        coEvery { homeDao.getCustomerOnce() } returns cachedCustomer()
        coEvery { remote.getCustomer() } returns remoteCustomer(name = "Remote User")

        repository.getCustomer().test {
            assertEquals(Resource.Loading, awaitItem())

            val cached = awaitItem() as Resource.Success
            assertEquals("Cached User", cached.data.name)

            val refreshed = awaitItem() as Resource.Success
            assertEquals("Remote User", refreshed.data.name)

            awaitComplete()
        }

        coVerify {
            homeDao.insertCustomer(match { it.name == "Remote User" && it.email == "remote@shopme.local" })
        }
    }

    @Test
    fun `getCustomer should keep cached customer when backend refresh fails`() = runTest {
        coEvery { homeDao.getCustomerOnce() } returns cachedCustomer()
        coEvery { remote.getCustomer() } throws IllegalStateException("backend down")

        repository.getCustomer().test {
            assertEquals(Resource.Loading, awaitItem())

            val cached = awaitItem() as Resource.Success
            assertEquals("Cached User", cached.data.name)

            awaitComplete()
        }
    }

    @Test
    fun `updateProfile should patch cached customer`() = runTest {
        coEvery { remote.updateProfile(any<CustomerUpdateParam>()) } returns Unit
        coEvery { homeDao.getCustomerOnce() } returns cachedCustomer()

        repository.updateProfile(
            CustomerUpdateParam(
                name = "Updated User",
                phone = "081999",
                photo = "updated-photo",
                fcmToken = null
            )
        ).test {
            assertEquals(Resource.Loading, awaitItem())
            awaitItem()
            awaitComplete()
        }

        coVerify {
            homeDao.insertCustomer(
                match {
                    it.name == "Updated User" &&
                        it.phone == "081999" &&
                        it.photo == "updated-photo"
                }
            )
        }
    }

    @Test
    fun `getCustomer should emit mapped error when cache is empty and backend fails`() = runTest {
        val mappedError = mockk<UiError>(relaxed = true)
        coEvery { homeDao.getCustomerOnce() } returns null
        coEvery { remote.getCustomer() } throws IllegalStateException("backend down")
        every { errorMapper.map(any()) } returns mappedError

        repository.getCustomer().test {
            assertEquals(Resource.Loading, awaitItem())
            val error = awaitItem() as Resource.Error
            assertEquals(mappedError, error.error)
            awaitComplete()
        }
    }

    private fun cachedCustomer() = CustomerEntity(
        id = "self",
        name = "Cached User",
        phone = "081234",
        email = "cached@shopme.local",
        addressVillage = "Cached Village",
        addressBlock = "A",
        addressNumber = "12",
        addressRt = "01",
        addressRw = "02",
        photo = "cached-photo",
        verified = true,
        totalOrders = 4,
        activeOrders = 1,
        membership = "Gold",
        ordered = 1,
        cooking = 1,
        shipping = 1,
        completed = 1,
        cancelled = 0,
        updatedAt = 1L
    )

    private fun remoteCustomer(name: String) = CustomerResponse(
        name = name,
        phone = "082222",
        email = "remote@shopme.local",
        address = AddressResponse(
            id = "address-1",
            village = "Remote Village",
            block = "B",
            number = "8",
            rt = "03",
            rw = "04",
            isDefault = true
        ),
        photo = "remote-photo",
        verified = true,
        stats = StatsResponse(
            totalOrders = 10,
            activeOrders = 2,
            membership = "Platinum"
        ),
        menuSummary = MenuSummaryResponse(
            ordered = 2,
            cooking = 3,
            shipping = 1,
            completed = 4,
            cancelled = 0
        )
    )
}
