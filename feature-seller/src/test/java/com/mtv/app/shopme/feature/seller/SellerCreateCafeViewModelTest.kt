package com.mtv.app.shopme.feature.seller

import com.mtv.app.shopme.domain.model.SellerProfile
import com.mtv.app.shopme.domain.model.Village
import com.mtv.app.shopme.domain.usecase.CreateCafeUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.domain.usecase.GetVillageUseCase
import com.mtv.app.shopme.domain.usecase.UpsertCafeAddressUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeEffect
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeEvent
import com.mtv.app.shopme.feature.seller.presentation.SellerCreateCafeViewModel
import com.mtv.based.core.network.utils.Resource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SellerCreateCafeViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val createCafeUseCase: CreateCafeUseCase = mockk()
    private val getSellerProfileUseCase: GetSellerProfileUseCase = mockk()
    private val getVillageUseCase: GetVillageUseCase = mockk()
    private val upsertCafeAddressUseCase: UpsertCafeAddressUseCase = mockk()

    @Test
    fun `create cafe should create cafe and address then navigate success`() = runTest {
        every { getVillageUseCase.invoke() } returns flowOf(Resource.Success(listOf(Village("v-1", "Kemang"))))
        every { createCafeUseCase.invoke(any()) } returns flowOf(Resource.Success(Unit))
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
        every { upsertCafeAddressUseCase.invoke(any()) } returns flowOf(Resource.Success(Unit))

        val vm = SellerCreateCafeViewModel(
            createCafeUseCase = createCafeUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase,
            getVillageUseCase = getVillageUseCase,
            upsertCafeAddressUseCase = upsertCafeAddressUseCase
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(SellerCreateCafeEvent.Load)
        advanceUntilIdle()
        vm.onEvent(SellerCreateCafeEvent.ChangeCafeName("Shopme Cafe"))
        vm.onEvent(SellerCreateCafeEvent.ChangePhone("0812"))
        vm.onEvent(SellerCreateCafeEvent.ChangeVillage("Kemang"))
        vm.onEvent(SellerCreateCafeEvent.CreateCafe)
        advanceUntilIdle()

        assertEquals(SellerCreateCafeEffect.NavigateSuccess, effect.await())
    }
}
