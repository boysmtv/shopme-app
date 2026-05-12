package com.mtv.app.shopme.feature.customer

import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEvent
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEventType
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeGateway
import com.mtv.app.shopme.domain.model.NotificationItem
import com.mtv.app.shopme.domain.usecase.ClearNotificationsUseCase
import com.mtv.app.shopme.domain.usecase.GetNotificationsUseCase
import com.mtv.app.shopme.feature.customer.contract.NotifEvent
import com.mtv.app.shopme.feature.customer.presentation.NotifViewModel
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

class NotifViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getNotificationsUseCase: GetNotificationsUseCase = mockk()
    private val clearNotificationsUseCase: ClearNotificationsUseCase = mockk(relaxed = true)
    private val realtimeEvents = MutableSharedFlow<ShopmeRealtimeEvent>(extraBufferCapacity = 4)
    private val realtimeGateway: ShopmeRealtimeGateway = mockk(relaxed = true)

    @Test
    fun `realtime notification should prepend locally without refetch`() = runTest {
        every { realtimeGateway.events } returns realtimeEvents
        every { getNotificationsUseCase.invoke() } returns flowOf(
            Resource.Success(
                listOf(
                    NotificationItem(
                        title = "Notif Lama",
                        message = "Pesan lama",
                        photo = "",
                        signatureName = "Notif Lama",
                        signatureDate = "2026-05-11",
                        signatureTime = "09:00:00",
                        isRead = true
                    )
                )
            )
        )

        val vm = NotifViewModel(getNotificationsUseCase, clearNotificationsUseCase, realtimeGateway)
        vm.onEvent(NotifEvent.Load)
        advanceUntilIdle()

        realtimeEvents.emit(
            ShopmeRealtimeEvent(
                type = ShopmeRealtimeEventType.NOTIFICATION_CREATED,
                title = "Notif Baru",
                message = "Pesan baru",
                occurredAt = "2026-05-12T21:00:00Z"
            )
        )
        advanceUntilIdle()

        assertEquals("Notif Baru", vm.uiState.value.localNotification.first().title)
        assertEquals("Pesan baru", vm.uiState.value.localNotification.first().message)
        assertEquals(LoadState.Success(""), vm.uiState.value.notificationState)
        verify(exactly = 1) { getNotificationsUseCase.invoke() }
    }
}
