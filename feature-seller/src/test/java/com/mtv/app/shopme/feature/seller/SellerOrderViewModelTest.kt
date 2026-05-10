package com.mtv.app.shopme.feature.seller

import com.mtv.app.shopme.domain.model.SellerProfile
import com.mtv.app.shopme.domain.usecase.GetSellerOrdersUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.domain.usecase.UpdateSellerAvailabilityUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerOrderEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerOrderViewModel
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

class SellerOrderViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val sessionManager: SessionManager = mockk(relaxed = true)
    private val getOrdersUseCase: GetSellerOrdersUseCase = mockk()
    private val getProfileUseCase: GetSellerProfileUseCase = mockk()
    private val updateAvailabilityUseCase: UpdateSellerAvailabilityUseCase = mockk()

    @Test
    fun `load and toggle online should follow backend profile state`() = runTest {
        every { getOrdersUseCase.invoke() } returns flowOf(Resource.Success(emptyList()))
        every { getProfileUseCase.invoke() } returns flowOf(
            Resource.Success(
                SellerProfile(
                    sellerName = "Seller",
                    email = "seller@mail.com",
                    phone = "0812",
                    storeName = "Shopme Cafe",
                    storeAddress = "Kemang",
                    isOnline = true,
                    hasCafe = true
                )
            )
        )
        every { updateAvailabilityUseCase.invoke(false) } returns flowOf(
            Resource.Success(
                SellerProfile(
                    sellerName = "Seller",
                    email = "seller@mail.com",
                    phone = "0812",
                    storeName = "Shopme Cafe",
                    storeAddress = "Kemang",
                    isOnline = false,
                    hasCafe = true
                )
            )
        )

        val vm = SellerOrderViewModel(
            sessionManager = sessionManager,
            getSellerOrdersUseCase = getOrdersUseCase,
            getSellerProfileUseCase = getProfileUseCase,
            updateSellerAvailabilityUseCase = updateAvailabilityUseCase
        )

        vm.onEvent(SellerOrderEvent.Load)
        advanceUntilIdle()
        assertEquals(true, vm.uiState.value.isOnline)

        vm.onEvent(SellerOrderEvent.ToggleOnline)
        advanceUntilIdle()
        assertEquals(false, vm.uiState.value.isOnline)
    }
}
