package com.mtv.app.shopme.feature.seller

import com.mtv.app.shopme.domain.model.SellerProfile
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.domain.usecase.UpdateSellerAvailabilityUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerStoreEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerProfileViewModel
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

class SellerProfileViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val sessionManager: SessionManager = mockk(relaxed = true)
    private val getProfileUseCase: GetSellerProfileUseCase = mockk()
    private val updateAvailabilityUseCase: UpdateSellerAvailabilityUseCase = mockk()

    @Test
    fun `toggle online should persist backend state`() = runTest {
        every { getProfileUseCase.invoke() } returns flowOf(
            Resource.Success(
                SellerProfile(
                    sellerName = "Seller",
                    sellerPhoto = "https://media.shopme.test/users/seller-1/medium.jpg",
                    email = "seller@mail.com",
                    phone = "0812",
                    storeName = "Shopme Cafe",
                    storePhoto = "https://media.shopme.test/cafes/cafe-1/medium.jpg",
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
                    sellerPhoto = "https://media.shopme.test/users/seller-1/medium.jpg",
                    email = "seller@mail.com",
                    phone = "0812",
                    storeName = "Shopme Cafe",
                    storePhoto = "https://media.shopme.test/cafes/cafe-1/medium.jpg",
                    storeAddress = "Kemang",
                    isOnline = false,
                    hasCafe = true
                )
            )
        )

        val vm = SellerProfileViewModel(
            sessionManager = sessionManager,
            getSellerProfileUseCase = getProfileUseCase,
            updateSellerAvailabilityUseCase = updateAvailabilityUseCase
        )

        vm.onEvent(SellerStoreEvent.Load)
        advanceUntilIdle()
        vm.onEvent(SellerStoreEvent.ToggleOnline)
        advanceUntilIdle()

        assertEquals(false, vm.uiState.value.isOnline)
        assertEquals("https://media.shopme.test/cafes/cafe-1/medium.jpg", vm.uiState.value.storePhoto)
    }
}
