package com.mtv.app.shopme.data

import app.cash.turbine.test
import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.database.entity.AppNotificationCacheEntity
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.remote.datasource.NotificationRemoteDataSource
import com.mtv.app.shopme.data.remote.response.AppNotificationResponse
import com.mtv.app.shopme.data.repository.NotificationRepositoryImpl
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
class NotificationRepositoryImplTest {

    private val remote: NotificationRemoteDataSource = mockk()
    private val homeDao: HomeDao = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk()

    private val repository = NotificationRepositoryImpl(
        remote = remote,
        resultFlow = ResultFlowFactory(errorMapper),
        homeDao = homeDao,
        errorMapper = errorMapper
    )

    @Test
    fun `getCustomerNotifications should emit cached notifications first then refresh`() = runTest {
        coEvery { homeDao.getNotificationsOnce("customer") } returns listOf(
            AppNotificationCacheEntity(
                cacheKey = "customer:notif-1",
                scope = "customer",
                notificationId = "notif-1",
                title = "Cached title",
                message = "Cached message",
                createdAt = "2026-05-12T09:00:00",
                isRead = false,
                updatedAt = 1L
            )
        )
        coEvery { remote.getNotifications() } returns listOf(
            AppNotificationResponse(
                id = "notif-2",
                title = "Remote title",
                message = "Remote message",
                isRead = true,
                createdAt = "2026-05-12T10:00:00"
            )
        )

        repository.getCustomerNotifications().test {
            assertEquals(Resource.Loading, awaitItem())

            val cached = awaitItem() as Resource.Success
            assertEquals("Cached title", cached.data.first().title)

            val refreshed = awaitItem() as Resource.Success
            assertEquals("Remote title", refreshed.data.first().title)

            awaitComplete()
        }

        coVerify { homeDao.clearNotifications("customer") }
        coVerify { homeDao.insertNotifications(match { it.single().notificationId == "notif-2" }) }
    }

    @Test
    fun `getSellerNotifications should emit mapped error when cache empty and backend fails`() = runTest {
        val mappedError = mockk<UiError>(relaxed = true)
        coEvery { homeDao.getNotificationsOnce("seller") } returns emptyList()
        coEvery { remote.getNotifications() } throws IllegalStateException("backend down")
        every { errorMapper.map(any()) } returns mappedError

        repository.getSellerNotifications().test {
            assertEquals(Resource.Loading, awaitItem())
            val error = awaitItem() as Resource.Error
            assertEquals(mappedError, error.error)
            awaitComplete()
        }
    }

    @Test
    fun `clearNotifications should clear local caches for both scopes`() = runTest {
        coEvery { remote.clearNotifications() } returns Unit

        repository.clearNotifications().test {
            assertEquals(Resource.Loading, awaitItem())
            awaitItem()
            awaitComplete()
        }

        coVerify { homeDao.clearNotifications("customer") }
        coVerify { homeDao.clearNotifications("seller") }
    }

    @Test
    fun `getUnreadCount should emit count from lightweight endpoint`() = runTest {
        coEvery { remote.getUnreadCount() } returns 7

        repository.getUnreadCount().test {
            assertEquals(Resource.Loading, awaitItem())
            val success = awaitItem() as Resource.Success
            assertEquals(7, success.data)
            awaitComplete()
        }
    }
}
