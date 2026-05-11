package com.mtv.app.shopme.feature.customer

import com.mtv.app.shopme.domain.model.NotificationPreferences
import com.mtv.app.shopme.domain.param.NotificationPreferencesParam
import com.mtv.app.shopme.domain.usecase.GetNotificationPreferencesUseCase
import com.mtv.app.shopme.domain.usecase.UpdateNotificationPreferencesUseCase
import com.mtv.app.shopme.feature.customer.contract.NotificationEvent
import com.mtv.app.shopme.feature.customer.presentation.NotificationViewModel
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SessionManager
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class NotificationViewModelTest {
    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getNotificationPreferencesUseCase: GetNotificationPreferencesUseCase = mockk()
    private val updateNotificationPreferencesUseCase: UpdateNotificationPreferencesUseCase = mockk()
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun `load should reflect backend notification preferences`() = runTest {
        every { getNotificationPreferencesUseCase.invoke() } returns flowOf(
            Resource.Success(
                NotificationPreferences(
                    orderNotification = false,
                    promoNotification = true,
                    chatNotification = true,
                    pushEnabled = false,
                    emailEnabled = true
                )
            )
        )

        val vm = NotificationViewModel(
            getNotificationPreferencesUseCase = getNotificationPreferencesUseCase,
            updateNotificationPreferencesUseCase = updateNotificationPreferencesUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(NotificationEvent.Load)
        advanceUntilIdle()

        assertEquals(false, vm.uiState.value.orderNotification)
        assertEquals(false, vm.uiState.value.pushEnabled)
        assertEquals(true, vm.uiState.value.emailEnabled)
    }

    @Test
    fun `toggle should persist updated preferences`() = runTest {
        every { getNotificationPreferencesUseCase.invoke() } returns flowOf(
            Resource.Success(
                NotificationPreferences(
                    orderNotification = true,
                    promoNotification = true,
                    chatNotification = true,
                    pushEnabled = true,
                    emailEnabled = false
                )
            )
        )
        every {
            updateNotificationPreferencesUseCase.invoke(
                NotificationPreferencesParam(
                    orderNotification = true,
                    promoNotification = true,
                    chatNotification = true,
                    pushEnabled = false,
                    emailEnabled = false
                )
            )
        } returns flowOf(
            Resource.Success(
                NotificationPreferences(
                    orderNotification = true,
                    promoNotification = true,
                    chatNotification = true,
                    pushEnabled = false,
                    emailEnabled = false
                )
            )
        )

        val vm = NotificationViewModel(
            getNotificationPreferencesUseCase = getNotificationPreferencesUseCase,
            updateNotificationPreferencesUseCase = updateNotificationPreferencesUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(NotificationEvent.Load)
        advanceUntilIdle()
        vm.onEvent(NotificationEvent.TogglePush(false))
        advanceUntilIdle()

        assertEquals(false, vm.uiState.value.pushEnabled)
    }

    @Test
    fun `unauthorized notification load should force logout`() = runTest {
        every { getNotificationPreferencesUseCase.invoke() } returns flowOf(
            Resource.Error(UiError.Unauthorized("Session expired"))
        )

        val vm = NotificationViewModel(
            getNotificationPreferencesUseCase = getNotificationPreferencesUseCase,
            updateNotificationPreferencesUseCase = updateNotificationPreferencesUseCase,
            sessionManager = sessionManager
        )

        vm.onEvent(NotificationEvent.Load)
        advanceUntilIdle()

        assertEquals(false, vm.uiState.value.loading)
        coVerify(exactly = 1) { sessionManager.forceLogout() }
    }
}
