/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotifViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 15.00
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEventType
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeGateway
import com.mtv.app.shopme.domain.usecase.ClearNotificationsUseCase
import com.mtv.app.shopme.domain.usecase.GetNotificationsUseCase
import com.mtv.app.shopme.feature.customer.contract.NotifDialog
import com.mtv.app.shopme.feature.customer.contract.NotifEffect
import com.mtv.app.shopme.feature.customer.contract.NotifEvent
import com.mtv.app.shopme.feature.customer.contract.NotifUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NotifViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val clearNotificationsUseCase: ClearNotificationsUseCase,
    private val realtimeGateway: ShopmeRealtimeGateway,
) : BaseEventViewModel<NotifEvent, NotifEffect>() {

    private val _state = MutableStateFlow(NotifUiState())
    val uiState = _state.asStateFlow()

    init {
        viewModelScope.launch {
            realtimeGateway.events.collectLatest { event ->
                if (event.type == ShopmeRealtimeEventType.NOTIFICATION_CREATED) {
                    getNotifications()
                }
            }
        }
    }

    override fun onEvent(event: NotifEvent) {
        when (event) {
            is NotifEvent.Load, is NotifEvent.GetNotification -> getNotifications()
            is NotifEvent.DismissDialog -> dismissDialog()
            is NotifEvent.ClearNotification -> clearNotifications()
            is NotifEvent.ClickNotification -> Unit
            is NotifEvent.ClickBack -> emitEffect(NotifEffect.NavigateBack)
        }
    }

    private fun getNotifications() {
        realtimeGateway.ensureConnected()
        observeDataFlow(
            flow = getNotificationsUseCase(),
            onState = { state ->
                _state.update {
                    it.copy(
                        notificationState = if (state is LoadState.Success) LoadState.Success("") else state.transform(),
                        localNotification = if (state is LoadState.Success) state.data else it.localNotification
                    )
                }
            },
            onError = { error ->
                _state.update { it.copy(activeDialog = NotifDialog.Error(error.message)) }
            }
        )
    }

    private fun clearNotifications() {
        observeDataFlow(
            flow = clearNotificationsUseCase(),
            onState = { state ->
                if (state is LoadState.Success) {
                    _state.update { it.copy(localNotification = emptyList(), activeDialog = NotifDialog.Success, notificationState = LoadState.Success("")) }
                } else {
                    _state.update { it.copy(notificationState = state.transform()) }
                }
            },
            onError = { error ->
                _state.update { it.copy(activeDialog = NotifDialog.Error(error.message)) }
            }
        )
    }

    private fun <T> LoadState<T>.transform(): LoadState<String> = when (this) {
        is LoadState.Idle -> LoadState.Idle
        is LoadState.Loading -> LoadState.Loading
        is LoadState.Success -> LoadState.Success("")
        is LoadState.Error -> LoadState.Error(this.error)
    }
}
