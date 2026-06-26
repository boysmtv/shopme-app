@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.mtv.app.shopme.feature.customer

import com.mtv.app.shopme.common.ConstantPreferences
import com.mtv.app.shopme.domain.model.Address
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.SellerProfile
import com.mtv.app.shopme.domain.usecase.GetCustomerUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.feature.customer.contract.ProfileEffect
import com.mtv.app.shopme.feature.customer.contract.ProfileEvent
import com.mtv.app.shopme.feature.customer.presentation.ProfileViewModel
import com.mtv.app.shopme.domain.model.Resource
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val securePrefs: SecurePrefs = mockk(relaxed = true)
    private val sessionManager: SessionManager = mockk(relaxed = true)
    private val customerUseCase: GetCustomerUseCase = mockk()
    private val getSellerProfileUseCase: GetSellerProfileUseCase = mockk()

    private val sampleCustomer = Customer(
        name = "John",
        phone = "08123456789",
        email = "john@mail.com",
        address = Address(
            id = "addr-1",
            village = "Tebet",
            block = "A",
            number = "12",
            rt = "001",
            rw = "005",
            isDefault = true
        ),
        photo = "",
        verified = true,
        stats = null,
        menuSummary = null
    )

    @Test
    fun `load should fetch customer data`() = runTest {
        every { customerUseCase.invoke() } returns flowOf(Resource.Success(sampleCustomer))
        val vm = ProfileViewModel(
            securePrefs = securePrefs,
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase
        )

        vm.onEvent(ProfileEvent.Load)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.customer is LoadState.Success)
        assertEquals("John", (vm.uiState.value.customer as LoadState.Success).data.name)
    }

    @Test
    fun `error loading customer should show error state`() = runTest {
        every { customerUseCase.invoke() } returns flowOf(
            Resource.Error(throwable = RuntimeException("Failed"))
        )
        val vm = ProfileViewModel(
            securePrefs = securePrefs,
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase
        )

        vm.onEvent(ProfileEvent.Load)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.customer is LoadState.Error)
    }

    @Test
    fun `click edit profile should navigate to edit profile`() = runTest {
        val vm = ProfileViewModel(
            securePrefs = securePrefs,
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(ProfileEvent.ClickEditProfile)
        advanceUntilIdle()

        assertEquals(ProfileEffect.NavigateToEditProfile, effect.await())
    }

    @Test
    fun `click order history should navigate to order history`() = runTest {
        val vm = ProfileViewModel(
            securePrefs = securePrefs,
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(ProfileEvent.ClickOrderHistory)
        advanceUntilIdle()

        assertEquals(ProfileEffect.NavigateToOrderHistory, effect.await())
    }

    @Test
    fun `click favorites should navigate to favorites`() = runTest {
        val vm = ProfileViewModel(
            securePrefs = securePrefs,
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(ProfileEvent.ClickFavorites)
        advanceUntilIdle()

        assertEquals(ProfileEffect.NavigateToFavorites, effect.await())
    }

    @Test
    fun `click settings should navigate to settings`() = runTest {
        val vm = ProfileViewModel(
            securePrefs = securePrefs,
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(ProfileEvent.ClickSettings)
        advanceUntilIdle()

        assertEquals(ProfileEffect.NavigateToSettings, effect.await())
    }

    @Test
    fun `click help center should navigate to help center`() = runTest {
        val vm = ProfileViewModel(
            securePrefs = securePrefs,
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(ProfileEvent.ClickHelpCenter)
        advanceUntilIdle()

        assertEquals(ProfileEffect.NavigateToHelpCenter, effect.await())
    }

    @Test
    fun `click order should navigate to order with filter`() = runTest {
        val vm = ProfileViewModel(
            securePrefs = securePrefs,
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(ProfileEvent.ClickOrder(filter = "DIPROSES"))
        advanceUntilIdle()

        assertEquals(ProfileEffect.NavigateToOrder(filter = "DIPROSES"), effect.await())
    }

    @Test
    fun `logout should clear prefs and emit navigate to login`() = runTest {
        every { securePrefs.getString(ConstantPreferences.REMEMBERED_LOGIN_EMAIL) } returns "john@mail.com"

        val vm = ProfileViewModel(
            securePrefs = securePrefs,
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(ProfileEvent.ClickLogout)
        advanceUntilIdle()

        verify(exactly = 1) { securePrefs.clear() }
        verify(exactly = 1) { securePrefs.putString(ConstantPreferences.REMEMBERED_LOGIN_EMAIL, "john@mail.com") }
        assertEquals(ProfileEffect.NavigateToLogin, effect.await())
    }

    @Test
    fun `check tnc when seller profile has cafe should navigate to seller`() = runTest {
        every { getSellerProfileUseCase.invoke() } returns flowOf(
            Resource.Success(SellerProfile(hasCafe = true, sellerName = "Kopi Kita", email = "", phone = "", storeName = "", storeAddress = "", isOnline = false))
        )

        val vm = ProfileViewModel(
            securePrefs = securePrefs,
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(ProfileEvent.ClickCheckTncCafe)
        advanceUntilIdle()

        assertEquals(ProfileEffect.NavigateToSeller, effect.await())
    }

    @Test
    fun `check tnc when seller has no cafe should navigate to tnc`() = runTest {
        every { getSellerProfileUseCase.invoke() } returns flowOf(
            Resource.Success(SellerProfile(hasCafe = false, sellerName = "", email = "", phone = "", storeName = "", storeAddress = "", isOnline = false))
        )

        val vm = ProfileViewModel(
            securePrefs = securePrefs,
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            getSellerProfileUseCase = getSellerProfileUseCase
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(ProfileEvent.ClickCheckTncCafe)
        advanceUntilIdle()

        assertEquals(ProfileEffect.NavigateToTnc, effect.await())
    }
}
