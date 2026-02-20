/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotificationViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 15.00
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.domain.NotificationUseCase
import com.mtv.app.shopme.feature.customer.contract.NotifDataListener
import com.mtv.app.shopme.feature.customer.contract.NotifStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NotifViewModel @Inject constructor(
    private val notificationUseCase: NotificationUseCase,
    private val sessionManager: SessionManager,
    private val securePrefs: SecurePrefs,
) : BaseViewModel(), UiOwner<NotifStateListener, NotifDataListener> {

    override val uiState = MutableStateFlow(NotifStateListener())
    override val uiData = MutableStateFlow(NotifDataListener())

    /*
    init {
        getLocalNotification()
    }

    fun getLocalNotification() = uiState.runStateLocalManager(
        block = {
            val localNotification = securePrefs.getObject(
                FCM_NOTIFICATION,
                Array<NotificationItem>::class.java
            )?.toList() ?: emptyList()

            updateUiDataListener(uiData) {
                copy(localNotification = localNotification)
            }
        },
        reducer = { state, _ -> state },
        onSuccess = {
            uiState.update {
                it.copy(
                    notificationState = ResourceFirebase.Success(EMPTY_STRING)
                )
            }
        },
        onError = { throwable ->
            uiState.update {
                it.copy(
                    activeDialog = NotificationDialog.Error(
                        message = throwable.message ?: ErrorMessages.GENERIC_ERROR
                    )
                )
            }
        }
    )

    fun doClearNotification() = uiState.runStateLocalManager(
        block = {
            securePrefs.remove(FCM_NOTIFICATION)

            updateUiDataListener(uiData) {
                copy(localNotification = emptyList())
            }
        },
        reducer = { state, _ -> state },
        onSuccess = {
            uiState.update {
                it.copy(activeDialog = NotificationDialog.Success)
            }
        },
        onError = { throwable ->
            uiState.update {
                it.copy(
                    activeDialog = NotificationDialog.Error(
                        message = throwable.message ?: ErrorMessages.GENERIC_ERROR
                    )
                )
            }
        }
    )

    fun doDismissActiveDialog() {
        uiState.update { it.copy(activeDialog = null) }
    }

    */
}
