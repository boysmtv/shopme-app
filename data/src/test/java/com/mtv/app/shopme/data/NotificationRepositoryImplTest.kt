package com.mtv.app.shopme.data

import app.cash.turbine.test
import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.database.entity.AppNotificationCacheEntity
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.remote.datasource.NotificationRemoteDataSource
import com.mtv.app.shopme.data.remote.response.AppNotificationResponse
import com.mtv.app.shopme.data.remote.response.PageResponse
import com.mtv.app.shopme.data.repository.NotificationRepositoryImpl
import com.mtv.app.shopme.domain.model.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationRepositoryImplTest {

    private val remote: NotificationRemoteDataSource = mockk()
    private val homeDao: HomeDao = mockk(relaxed = true)
    private val repository = NotificationRepositoryImpl(
        remote = remote,
        resultFlow = ResultFlowFactory(),
        homeDao = homeDao,
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
    fun `getSellerNotifications should emit error when cache empty and backend fails`() = runTest {
        coEvery { homeDao.getNotificationsOnce("seller") } returns emptyList()
        coEvery { remote.getNotifications() } throws IllegalStateException("backend down")

        repository.getSellerNotifications().test {
            assertEquals(Resource.Loading, awaitItem())
            val error = awaitItem() as Resource.Error
            assertEquals("backend down", error.throwable?.message)
            awaitComplete()
        }
    }

    @Test
    fun `getCustomerNotifications page should use paged endpoint and cache first page`() = runTest {
        coEvery { homeDao.getNotificationsOnce("customer") } returns emptyList()
        coEvery { remote.getNotifications(0, 20) } returns PageResponse(
            content = listOf(
                AppNotificationResponse(
                    id = "notif-3",
                    title = "Paged title",
                    message = "Paged message",
                    isRead = false,
                    createdAt = "2026-05-12T11:00:00"
                )
            ),
            page = 0,
            size = 20,
            totalElements = 1,
            totalPages = 1,
            last = true
        )

        repository.getCustomerNotifications(page = 0, size = 20).test {
            assertEquals(Resource.Loading, awaitItem())
            val refreshed = awaitItem() as Resource.Success
            assertEquals("Paged title", refreshed.data.content.first().title)
            assertEquals(true, refreshed.data.last)
            awaitComplete()
        }

        coVerify { homeDao.clearNotifications("customer") }
        coVerify { homeDao.insertNotifications(match { it.single().notificationId == "notif-3" }) }
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
