package com.mtv.app.shopme.feature.customer

import com.mtv.app.shopme.domain.usecase.DeleteAccountUseCase
import com.mtv.app.shopme.feature.customer.contract.SecurityEffect
import com.mtv.app.shopme.feature.customer.contract.SecurityEvent
import com.mtv.app.shopme.feature.customer.presentation.SecurityViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.provider.utils.SecurePrefs
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SecurityViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val securePrefs: SecurePrefs = mockk(relaxed = true)
    private val deleteAccountUseCase: DeleteAccountUseCase = mockk()

    @Test
    fun `logout all device should clear local session and emit logout success`() = runTest {
        val vm = SecurityViewModel(
            securePrefs = securePrefs,
            deleteAccountUseCase = deleteAccountUseCase
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(SecurityEvent.LogoutAllDevice)
        advanceUntilIdle()

        verify(exactly = 1) { securePrefs.clear() }
        assertEquals(SecurityEffect.LogoutSuccess, effect.await())
    }

    @Test
    fun `delete account should call backend then clear local session`() = runTest {
        every { deleteAccountUseCase.invoke() } returns flowOf(Resource.Success(Unit))

        val vm = SecurityViewModel(
            securePrefs = securePrefs,
            deleteAccountUseCase = deleteAccountUseCase
        )
        val effect = async { vm.effect.first() }

        vm.onEvent(SecurityEvent.DeleteAccount)
        advanceUntilIdle()

        verify(exactly = 1) { deleteAccountUseCase.invoke() }
        verify(exactly = 1) { securePrefs.clear() }
        assertEquals(SecurityEffect.DeleteAccountSuccess, effect.await())
    }
}
