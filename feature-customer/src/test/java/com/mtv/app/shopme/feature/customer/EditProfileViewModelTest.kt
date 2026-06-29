@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.mtv.app.shopme.feature.customer

import com.mtv.app.shopme.domain.model.Address
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.Village
import com.mtv.app.shopme.domain.param.CustomerUpdateParam
import com.mtv.app.shopme.domain.usecase.CreateAddressUseCase
import com.mtv.app.shopme.domain.usecase.DeleteAddressUseCase
import com.mtv.app.shopme.domain.usecase.GetAddressUseCase
import com.mtv.app.shopme.domain.usecase.GetCustomerUseCase
import com.mtv.app.shopme.domain.usecase.GetVillageUseCase
import com.mtv.app.shopme.domain.usecase.UpdateAddressDefaultUseCase
import com.mtv.app.shopme.domain.usecase.UpdateAddressUseCase
import com.mtv.app.shopme.domain.usecase.UpdateCustomerUseCase
import com.mtv.app.shopme.domain.usecase.UploadMediaUseCase
import com.mtv.app.shopme.feature.customer.contract.EditProfileDialog
import com.mtv.app.shopme.feature.customer.contract.EditProfileEffect
import com.mtv.app.shopme.feature.customer.contract.EditProfileEvent
import com.mtv.app.shopme.feature.customer.presentation.EditProfileViewModel
import com.mtv.app.shopme.domain.model.Resource
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EditProfileViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val sessionManager: SessionManager = mockk(relaxed = true)
    private val customerUseCase: GetCustomerUseCase = mockk()
    private val customerUpdateUseCase: UpdateCustomerUseCase = mockk()
    private val addressUseCase: GetAddressUseCase = mockk()
    private val addressAddUseCase: CreateAddressUseCase = mockk()
    private val addressDeleteUseCase: DeleteAddressUseCase = mockk()
    private val addressUpdateUseCase: UpdateAddressUseCase = mockk()
    private val addressDefaultUseCase: UpdateAddressDefaultUseCase = mockk()
    private val villageUseCase: GetVillageUseCase = mockk()
    private val uploadMediaUseCase: UploadMediaUseCase = mockk()

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
    fun `load should fetch customer addresses and villages`() = runTest(dispatcherRule.testDispatcher) {
        every { customerUseCase.invoke() } returns flowOf(Resource.Success(sampleCustomer))
        every { addressUseCase.invoke() } returns flowOf(Resource.Success(emptyList<Address>()))
        every { villageUseCase.invoke() } returns flowOf(Resource.Success(emptyList<Village>()))
        val vm = EditProfileViewModel(
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            customerUpdateUseCase = customerUpdateUseCase,
            addressUseCase = addressUseCase,
            addressAddUseCase = addressAddUseCase,
            addressDeleteUseCase = addressDeleteUseCase,
            addressUpdateUseCase = addressUpdateUseCase,
            addressDefaultUseCase = addressDefaultUseCase,
            villageUseCase = villageUseCase,
            uploadMediaUseCase = uploadMediaUseCase
        )

        vm.onEvent(EditProfileEvent.Load)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.customer is LoadState.Success)
        assertTrue(vm.uiState.value.addresses is LoadState.Success)
        assertTrue(vm.uiState.value.villages is LoadState.Success)
    }

    @Test
    fun `update profile should succeed when photo reference is already a URL`() = runTest(dispatcherRule.testDispatcher) {
        every {
            customerUpdateUseCase.invoke(
                CustomerUpdateParam(name = "John Updated", phone = "08987654321", photo = "http://img.url", fcmToken = null)
            )
        } returns flowOf(Resource.Success(Unit))
        val vm = EditProfileViewModel(
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            customerUpdateUseCase = customerUpdateUseCase,
            addressUseCase = addressUseCase,
            addressAddUseCase = addressAddUseCase,
            addressDeleteUseCase = addressDeleteUseCase,
            addressUpdateUseCase = addressUpdateUseCase,
            addressDefaultUseCase = addressDefaultUseCase,
            villageUseCase = villageUseCase,
            uploadMediaUseCase = uploadMediaUseCase
        )

        vm.onEvent(
            EditProfileEvent.UpdateProfile(
                name = "John Updated",
                phone = "08987654321",
                photo = "http://img.url"
            )
        )
        advanceUntilIdle()

        assertTrue(vm.uiState.value.updateProfile is LoadState.Success)
        assertEquals(EditProfileDialog.SuccessUpdateProfile, vm.uiState.value.activeDialog)
    }

    @Test
    fun `click back should emit navigate back effect`() = runTest(dispatcherRule.testDispatcher) {
        val vm = EditProfileViewModel(
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            customerUpdateUseCase = customerUpdateUseCase,
            addressUseCase = addressUseCase,
            addressAddUseCase = addressAddUseCase,
            addressDeleteUseCase = addressDeleteUseCase,
            addressUpdateUseCase = addressUpdateUseCase,
            addressDefaultUseCase = addressDefaultUseCase,
            villageUseCase = villageUseCase,
            uploadMediaUseCase = uploadMediaUseCase
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(EditProfileEvent.ClickBack)
        advanceUntilIdle()

        assertEquals(EditProfileEffect.NavigateBack, effect.await())
    }

    @Test
    fun `load error should handle gracefully`() = runTest(dispatcherRule.testDispatcher) {
        every { customerUseCase.invoke() } returns flowOf(Resource.Error(throwable = RuntimeException("Network error")))
        every { addressUseCase.invoke() } returns flowOf(Resource.Success(emptyList<Address>()))
        every { villageUseCase.invoke() } returns flowOf(Resource.Success(emptyList<Village>()))
        val vm = EditProfileViewModel(
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            customerUpdateUseCase = customerUpdateUseCase,
            addressUseCase = addressUseCase,
            addressAddUseCase = addressAddUseCase,
            addressDeleteUseCase = addressDeleteUseCase,
            addressUpdateUseCase = addressUpdateUseCase,
            addressDefaultUseCase = addressDefaultUseCase,
            villageUseCase = villageUseCase,
            uploadMediaUseCase = uploadMediaUseCase
        )

        vm.onEvent(EditProfileEvent.Load)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.customer is LoadState.Error)
    }

    @Test
    fun `dismiss active dialog should clear it`() = runTest(dispatcherRule.testDispatcher) {
        val vm = EditProfileViewModel(
            sessionManager = sessionManager,
            customerUseCase = customerUseCase,
            customerUpdateUseCase = customerUpdateUseCase,
            addressUseCase = addressUseCase,
            addressAddUseCase = addressAddUseCase,
            addressDeleteUseCase = addressDeleteUseCase,
            addressUpdateUseCase = addressUpdateUseCase,
            addressDefaultUseCase = addressDefaultUseCase,
            villageUseCase = villageUseCase,
            uploadMediaUseCase = uploadMediaUseCase
        )

        vm.onEvent(EditProfileEvent.DismissActiveDialog)

        assertEquals(null, vm.uiState.value.activeDialog)
    }
}
