/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotifViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 15.00
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.data.local.NotificationItem
import com.mtv.app.shopme.domain.usecase.CreateNotificationUseCase
import com.mtv.app.shopme.feature.customer.contract.NotifDialog
import com.mtv.app.shopme.feature.customer.contract.NotifEffect
import com.mtv.app.shopme.feature.customer.contract.NotifEvent
import com.mtv.app.shopme.feature.customer.contract.NotifUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.ResourceFirebase
import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.based.core.provider.utils.SessionManager
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class NotifViewModel @Inject constructor(
    private val notificationUseCase: CreateNotificationUseCase,
    private val sessionManager: SessionManager,
    private val securePrefs: SecurePrefs,
) : BaseEventViewModel<NotifEvent, NotifEffect>() {

    private val _state = MutableStateFlow(NotifUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: NotifEvent) {
        when (event) {
            is NotifEvent.Load -> getLocalNotification()
            is NotifEvent.DismissDialog -> dismissDialog()

            is NotifEvent.GetNotification -> getLocalNotification()
            is NotifEvent.ClearNotification -> clearNotification()

            is NotifEvent.ClickNotification -> {
                // kalau nanti mau navigate detail notif → emitEffect di sini
            }

            is NotifEvent.ClickBack -> emitEffect(NotifEffect.NavigateBack)
        }
    }

    private fun getLocalNotification() {
        try {
            val localNotification = securePrefs.getObject(
                FCM_NOTIFICATION,
                Array<NotificationItem>::class.java
            )?.toList() ?: emptyList()

            _state.update {
                it.copy(
                    localNotification = localNotification,
                    notificationState = ResourceFirebase.Success(EMPTY_STRING)
                )
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    activeDialog = NotifDialog.Error(
                        message = e.message ?: ErrorMessages.GENERIC_ERROR
                    )
                )
            }
        }
    }

    private fun clearNotification() {
        try {
            securePrefs.remove(FCM_NOTIFICATION)

            _state.update {
                it.copy(
                    localNotification = emptyList(),
                    activeDialog = NotifDialog.Success
                )
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    activeDialog = NotifDialog.Error(
                        message = e.message ?: ErrorMessages.GENERIC_ERROR
                    )
                )
            }
        }
    }
}