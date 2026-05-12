package com.mtv.app.shopme.feature.seller

import app.cash.turbine.test
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeGateway
import com.mtv.app.shopme.domain.model.SellerOrderItem
import com.mtv.app.shopme.domain.model.SellerProfile
import com.mtv.app.shopme.domain.usecase.GetSellerOrdersUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.domain.usecase.GetUnreadNotificationCountUseCase
import com.mtv.app.shopme.domain.usecase.UpdateSellerAvailabilityUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEffect
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
    private val getSellerProfileUseCase: GetSellerProfileUseCase = mockk()
    private val getUnreadNotificationCountUseCase: GetUnreadNotificationCountUseCase = mockk()
    private val updateSellerAvailabilityUseCase: UpdateSellerAvailabilityUseCase = mockk(relaxed = true)
    private val realtimeGateway: ShopmeRealtimeGateway = mockk(relaxed = true)

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
        every { getUnreadNotificationCountUseCase.invoke() } returns flowOf(Resource.Success(1))

        val vm = SellerDashboardViewModel(
            sessionManager = sessionManager,
            getSellerOrdersUseCase = getSellerOrdersUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase,
            getUnreadNotificationCountUseCase = getUnreadNotificationCountUseCase,
            updateSellerAvailabilityUseCase = updateSellerAvailabilityUseCase,
            realtimeGateway = realtimeGateway
        )

        vm.onEvent(SellerDashboardEvent.Load)
        advanceUntilIdle()

        assertEquals("Cafe Backend", vm.uiState.value.storeName)
        assertEquals("Kemang - Blok A1", vm.uiState.value.storeAddress)
        assertEquals(1, vm.uiState.value.orders.size)
        assertEquals(1, vm.uiState.value.notificationCount)
    }

    @Test
    fun `click order detail should emit navigation effect with order id`() = runTest {
        every { getSellerProfileUseCase.invoke() } returns flowOf(Resource.Success(defaultProfile()))
        every { getSellerOrdersUseCase.invoke() } returns flowOf(Resource.Success(emptyList()))
        every { getUnreadNotificationCountUseCase.invoke() } returns flowOf(Resource.Success(0))

        val vm = SellerDashboardViewModel(
            sessionManager = sessionManager,
            getSellerOrdersUseCase = getSellerOrdersUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase,
            getUnreadNotificationCountUseCase = getUnreadNotificationCountUseCase,
            updateSellerAvailabilityUseCase = updateSellerAvailabilityUseCase,
            realtimeGateway = realtimeGateway
        )

        vm.effect.test {
            vm.onEvent(SellerDashboardEvent.ClickOrderDetail("order-77"))
            assertEquals(SellerDashboardEffect.NavigateToOrderDetail("order-77"), awaitItem())
        }
    }

    @Test
    fun `change filter and sort should update dashboard ui state`() = runTest {
        every { getSellerProfileUseCase.invoke() } returns flowOf(Resource.Success(defaultProfile()))
        every { getSellerOrdersUseCase.invoke() } returns flowOf(Resource.Success(emptyList()))
        every { getUnreadNotificationCountUseCase.invoke() } returns flowOf(Resource.Success(0))

        val vm = SellerDashboardViewModel(
            sessionManager = sessionManager,
            getSellerOrdersUseCase = getSellerOrdersUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase,
            getUnreadNotificationCountUseCase = getUnreadNotificationCountUseCase,
            updateSellerAvailabilityUseCase = updateSellerAvailabilityUseCase,
            realtimeGateway = realtimeGateway
        )

        vm.onEvent(SellerDashboardEvent.ChangeFilter("DELIVERING"))
        vm.onEvent(SellerDashboardEvent.ChangeSort("Desc"))
        advanceUntilIdle()

        assertEquals("DELIVERING", vm.uiState.value.selectedFilter)
        assertEquals("Desc", vm.uiState.value.selectedSort)
    }

    private fun defaultProfile() = SellerProfile(
        cafeId = "cafe-1",
        sellerName = "Seller",
        email = "seller@example.com",
        phone = "0812",
        storeName = "Cafe Backend",
        storeAddress = "Kemang - Blok A1",
        isOnline = true,
        hasCafe = true
    )
}
