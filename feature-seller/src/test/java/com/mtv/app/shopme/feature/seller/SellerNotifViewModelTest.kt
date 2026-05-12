package com.mtv.app.shopme.feature.seller

import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEvent
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEventType
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeGateway
import com.mtv.app.shopme.domain.model.SellerNotifItem
import com.mtv.app.shopme.domain.usecase.ClearNotificationsUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerNotificationsUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerNotifEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerNotifViewModel
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.Resource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SellerNotifViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getSellerNotificationsUseCase: GetSellerNotificationsUseCase = mockk()
    private val clearNotificationsUseCase: ClearNotificationsUseCase = mockk(relaxed = true)
    private val realtimeEvents = MutableSharedFlow<ShopmeRealtimeEvent>(extraBufferCapacity = 4)
    private val realtimeGateway: ShopmeRealtimeGateway = mockk(relaxed = true)
    private val sessionManager = mockk<com.mtv.based.core.provider.utils.SessionManager>(relaxed = true)

    @Test
    fun `seller realtime notification should prepend locally without refetch`() = runTest {
        every { realtimeGateway.events } returns realtimeEvents
        every { getSellerNotificationsUseCase.invoke() } returns flowOf(
            Resource.Success(
                listOf(
                    SellerNotifItem(
                        title = "Notif Lama",
                        message = "Pesan lama",
                        orderId = "old-1",
                        buyerName = "Notif Lama",
                        date = "2026-05-11",
                        time = "09:00:00",
                        isRead = true
                    )
                )
            )
        )

        val vm = SellerNotifViewModel(
            getSellerNotificationsUseCase,
            clearNotificationsUseCase,
            realtimeGateway,
            sessionManager
        )
        vm.onEvent(SellerNotifEvent.Load)
        advanceUntilIdle()

        realtimeEvents.emit(
            ShopmeRealtimeEvent(
                type = ShopmeRealtimeEventType.NOTIFICATION_CREATED,
                title = "Notif Baru",
                message = "Pesan baru",
                notificationId = "notif-2",
                occurredAt = "2026-05-12T21:05:00Z"
            )
        )
        advanceUntilIdle()

        assertEquals("Notif Baru", vm.uiState.value.notifications.first().title)
        assertEquals("notif-2", vm.uiState.value.notifications.first().orderId)
        verify(exactly = 1) { getSellerNotificationsUseCase.invoke() }
    }
}
