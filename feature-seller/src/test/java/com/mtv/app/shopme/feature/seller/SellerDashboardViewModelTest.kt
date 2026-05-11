package com.mtv.app.shopme.feature.seller

import com.mtv.app.shopme.domain.model.SellerNotifItem
import com.mtv.app.shopme.domain.model.SellerOrderItem
import com.mtv.app.shopme.domain.model.SellerProfile
import com.mtv.app.shopme.domain.usecase.GetSellerNotificationsUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerOrdersUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.domain.usecase.UpdateSellerAvailabilityUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerDashboardViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SellerDashboardViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val sessionManager: SessionManager = mockk(relaxed = true)
    private val getSellerOrdersUseCase: GetSellerOrdersUseCase = mockk()
    private val getSellerNotificationsUseCase: GetSellerNotificationsUseCase = mockk()
    private val getSellerProfileUseCase: GetSellerProfileUseCase = mockk()
    private val updateSellerAvailabilityUseCase: UpdateSellerAvailabilityUseCase = mockk(relaxed = true)

    @Test
    fun `load should reflect backend profile orders and unread notification count`() = runTest {
        every { getSellerProfileUseCase.invoke() } returns flowOf(
            Resource.Success(
                SellerProfile(
                    cafeId = "cafe-1",
                    sellerName = "Seller",
                    email = "seller@example.com",
                    phone = "0812",
                    storeName = "Cafe Backend",
                    storeAddress = "Kemang - Blok A1",
                    isOnline = true,
                    hasCafe = true
                )
            )
        )
        every { getSellerOrdersUseCase.invoke() } returns flowOf(
            Resource.Success(
                listOf(
                    SellerOrderItem(
                        id = "order-1",
                        invoice = "INV-1",
                        customer = "Buyer",
                        total = "Rp 18,000",
                        date = "11 May 2026",
                        time = "10:00",
                        paymentMethod = "TRANSFER",
                        status = "COMPLETED",
                        location = "Kemang"
                    )
                )
            )
        )
        every { getSellerNotificationsUseCase.invoke() } returns flowOf(
            Resource.Success(
                listOf(
                    SellerNotifItem(
                        title = "Order baru",
                        message = "Ada order baru",
                        orderId = "order-1",
                        buyerName = "Buyer",
                        date = "11 May 2026",
                        time = "10:00",
                        isRead = false
                    ),
                    SellerNotifItem(
                        title = "Order dibaca",
                        message = "Sudah dibaca",
                        orderId = "order-2",
                        buyerName = "Buyer",
                        date = "11 May 2026",
                        time = "11:00",
                        isRead = true
                    )
                )
            )
        )

        val vm = SellerDashboardViewModel(
            sessionManager = sessionManager,
            getSellerOrdersUseCase = getSellerOrdersUseCase,
            getSellerNotificationsUseCase = getSellerNotificationsUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase,
            updateSellerAvailabilityUseCase = updateSellerAvailabilityUseCase
        )

        vm.onEvent(SellerDashboardEvent.Load)
        advanceUntilIdle()

        assertEquals("Cafe Backend", vm.uiState.value.storeName)
        assertEquals("Kemang - Blok A1", vm.uiState.value.storeAddress)
        assertEquals(1, vm.uiState.value.orders.size)
        assertEquals(1, vm.uiState.value.notificationCount)
    }
}
