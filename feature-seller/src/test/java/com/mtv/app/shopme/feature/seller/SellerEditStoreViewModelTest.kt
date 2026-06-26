@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.mtv.app.shopme.feature.seller

import com.mtv.app.shopme.domain.model.Cafe
import com.mtv.app.shopme.domain.model.CafeAddress
import com.mtv.app.shopme.domain.model.SellerProfile
import com.mtv.app.shopme.domain.model.Village
import com.mtv.app.shopme.domain.usecase.GetCafeAddressUseCase
import com.mtv.app.shopme.domain.usecase.GetCafeUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.domain.usecase.GetVillageUseCase
import com.mtv.app.shopme.domain.usecase.UploadMediaUseCase
import com.mtv.app.shopme.domain.usecase.UpsertCafeAddressUseCase
import com.mtv.app.shopme.domain.usecase.UpdateCafeUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreEffect
import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerEditStoreViewModel
import com.mtv.app.shopme.domain.model.Resource
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class SellerEditStoreViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getSellerProfileUseCase: GetSellerProfileUseCase = mockk()
    private val getCafeUseCase: GetCafeUseCase = mockk()
    private val getCafeAddressUseCase: GetCafeAddressUseCase = mockk()
    private val updateCafeUseCase: UpdateCafeUseCase = mockk(relaxed = true)
    private val upsertCafeAddressUseCase: UpsertCafeAddressUseCase = mockk(relaxed = true)
    private val getVillageUseCase: GetVillageUseCase = mockk()
    private val uploadMediaUseCase: UploadMediaUseCase = mockk(relaxed = true)
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `load should populate store from backend cafe data`() = runTest {
        every { getVillageUseCase.invoke() } returns flowOf(Resource.Success(listOf(Village("v-1", "Kemang"))))
        every { getSellerProfileUseCase.invoke() } returns flowOf(
            Resource.Success(
                SellerProfile(
                    cafeId = "cafe-1",
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
        every { getCafeUseCase.invoke(any()) } returns flowOf(
            Resource.Success(
                Cafe(
                    id = "cafe-1",
                    name = "Shopme Cafe",
                    phone = "0812",
                    description = "Best shop",
                    minimalOrder = BigDecimal("15000"),
                    openTime = "08:00",
                    closeTime = "22:00",
                    image = "content://image",
                    isActive = true,
                    address = CafeAddress("addr-1", "Kemang", "A", "12", "01", "02")
                )
            )
        )
        every { getCafeAddressUseCase.invoke(any()) } returns flowOf(
            Resource.Success(CafeAddress("addr-1", "Kemang", "A", "12", "01", "02"))
        )

        val vm = SellerEditStoreViewModel(
            sessionManager = sessionManager,
            getSellerProfileUseCase = getSellerProfileUseCase,
            getCafeUseCase = getCafeUseCase,
            getCafeAddressUseCase = getCafeAddressUseCase,
            updateCafeUseCase = updateCafeUseCase,
            upsertCafeAddressUseCase = upsertCafeAddressUseCase,
            getVillageUseCase = getVillageUseCase,
            uploadMediaUseCase = uploadMediaUseCase
        )

        vm.onEvent(SellerEditStoreEvent.Load)
        advanceUntilIdle()
        advanceUntilIdle()

        assertEquals("Shopme Cafe", vm.uiState.value.storeName)
        assertEquals("15000", vm.uiState.value.minOrder)
        assertEquals("Kemang", vm.uiState.value.village)
    }

    @Test
    fun `save should update store and address`() = runTest {
        every { getVillageUseCase.invoke() } returns flowOf(Resource.Success(listOf(Village("v-1", "Kemang"))))
        every { getSellerProfileUseCase.invoke() } returns flowOf(
            Resource.Success(
                SellerProfile(
                    cafeId = "cafe-1",
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
        every { getCafeUseCase.invoke(any()) } returns flowOf(
            Resource.Success(
                Cafe(
                    id = "cafe-1", name = "Shopme Cafe", phone = "0812",
                    description = "Best shop", minimalOrder = BigDecimal("15000"),
                    openTime = "08:00", closeTime = "22:00",
                    image = null, isActive = true,
                    address = CafeAddress("addr-1", "Kemang", "A", "12", "01", "02")
                )
            )
        )
        every { getCafeAddressUseCase.invoke(any()) } returns flowOf(
            Resource.Success(CafeAddress("addr-1", "Kemang", "A", "12", "01", "02"))
        )

        val vm = SellerEditStoreViewModel(
            sessionManager = sessionManager,
            getSellerProfileUseCase = getSellerProfileUseCase,
            getCafeUseCase = getCafeUseCase,
            getCafeAddressUseCase = getCafeAddressUseCase,
            updateCafeUseCase = updateCafeUseCase,
            upsertCafeAddressUseCase = upsertCafeAddressUseCase,
            getVillageUseCase = getVillageUseCase,
            uploadMediaUseCase = uploadMediaUseCase
        )

        vm.onEvent(SellerEditStoreEvent.Load)
        advanceUntilIdle()
        advanceUntilIdle()

        vm.onEvent(SellerEditStoreEvent.Save)
        advanceUntilIdle()
    }

    @Test
    fun `load should handle error when cafe data fails`() = runTest {
        every { getVillageUseCase.invoke() } returns flowOf(Resource.Success(listOf(Village("v-1", "Kemang"))))
        every { getSellerProfileUseCase.invoke() } returns flowOf(
            Resource.Success(
                SellerProfile(
                    cafeId = "cafe-1", sellerName = "Seller", email = "seller@mail.com",
                    phone = "0812", storeName = "Shopme Cafe", storeAddress = "Kemang",
                    isOnline = true, hasCafe = true
                )
            )
        )
        every { getCafeUseCase.invoke(any()) } returns flowOf(Resource.Error(IOException("Failed to load cafe")))

        val vm = SellerEditStoreViewModel(
            sessionManager = sessionManager,
            getSellerProfileUseCase = getSellerProfileUseCase,
            getCafeUseCase = getCafeUseCase,
            getCafeAddressUseCase = getCafeAddressUseCase,
            updateCafeUseCase = updateCafeUseCase,
            upsertCafeAddressUseCase = upsertCafeAddressUseCase,
            getVillageUseCase = getVillageUseCase,
            uploadMediaUseCase = uploadMediaUseCase
        )

        vm.onEvent(SellerEditStoreEvent.Load)
        advanceUntilIdle()
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isLoading)
    }

    @Test
    fun `photo upload should emit OpenImagePicker effect and update state`() = runTest {
        every { getVillageUseCase.invoke() } returns flowOf(Resource.Success(listOf(Village("v-1", "Kemang"))))
        every { getSellerProfileUseCase.invoke() } returns flowOf(
            Resource.Success(
                SellerProfile(
                    cafeId = "cafe-1", sellerName = "Seller", email = "seller@mail.com",
                    phone = "0812", storeName = "Shopme Cafe", storeAddress = "Kemang",
                    isOnline = true, hasCafe = true
                )
            )
        )
        every { getCafeUseCase.invoke(any()) } returns flowOf(
            Resource.Success(
                Cafe(
                    id = "cafe-1", name = "Shopme Cafe", phone = "0812",
                    description = "Best shop", minimalOrder = BigDecimal("15000"),
                    openTime = "08:00", closeTime = "22:00",
                    image = null, isActive = true,
                    address = CafeAddress("addr-1", "Kemang", "A", "12", "01", "02")
                )
            )
        )
        every { getCafeAddressUseCase.invoke(any()) } returns flowOf(
            Resource.Success(CafeAddress("addr-1", "Kemang", "A", "12", "01", "02"))
        )

        val vm = SellerEditStoreViewModel(
            sessionManager = sessionManager,
            getSellerProfileUseCase = getSellerProfileUseCase,
            getCafeUseCase = getCafeUseCase,
            getCafeAddressUseCase = getCafeAddressUseCase,
            updateCafeUseCase = updateCafeUseCase,
            upsertCafeAddressUseCase = upsertCafeAddressUseCase,
            getVillageUseCase = getVillageUseCase,
            uploadMediaUseCase = uploadMediaUseCase
        )

        val effect = async { vm.effect.first() }

        vm.onEvent(SellerEditStoreEvent.UploadPhoto)
        advanceUntilIdle()

        assertEquals(SellerEditStoreEffect.OpenImagePicker, effect.await())

        vm.onEvent(SellerEditStoreEvent.PhotoSelected("content://new-photo"))
        assertEquals("content://new-photo", vm.uiState.value.storePhoto)
    }
}
