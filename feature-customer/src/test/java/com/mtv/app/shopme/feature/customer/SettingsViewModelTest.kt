package com.mtv.app.shopme.feature.customer

import com.mtv.app.shopme.domain.model.NotificationPreferences
import com.mtv.app.shopme.domain.usecase.GetNotificationPreferencesUseCase
import com.mtv.app.shopme.feature.customer.contract.SettingsEvent
import com.mtv.app.shopme.feature.customer.presentation.SettingsViewModel
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

class SettingsViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val sessionManager: SessionManager = mockk(relaxed = true)
    private val getNotificationPreferencesUseCase: GetNotificationPreferencesUseCase = mockk()

    @Test
    fun `load should mark notifications disabled when backend preferences are off`() = runTest {
        every { getNotificationPreferencesUseCase.invoke() } returns flowOf(
            Resource.Success(
                NotificationPreferences(
                    orderNotification = false,
                    promoNotification = false,
                    chatNotification = false,
                    pushEnabled = false,
                    emailEnabled = false
                )
            )
        )

        val vm = SettingsViewModel(
            sessionManager = sessionManager,
            getNotificationPreferencesUseCase = getNotificationPreferencesUseCase
        )

        vm.onEvent(SettingsEvent.Load)
        advanceUntilIdle()

        assertEquals(false, vm.uiState.value.notificationEnabled)
    }
}
