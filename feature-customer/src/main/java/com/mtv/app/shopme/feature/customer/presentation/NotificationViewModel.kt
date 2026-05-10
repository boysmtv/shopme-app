/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotificationViewModel.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.26
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.NotificationPreferences
import com.mtv.app.shopme.domain.param.NotificationPreferencesParam
import com.mtv.app.shopme.domain.usecase.GetNotificationPreferencesUseCase
import com.mtv.app.shopme.domain.usecase.UpdateNotificationPreferencesUseCase
import com.mtv.app.shopme.feature.customer.contract.NotificationEffect
import com.mtv.app.shopme.feature.customer.contract.NotificationEvent
import com.mtv.app.shopme.feature.customer.contract.NotificationUiState
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
import kotlinx.coroutines.flow.update

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationPreferencesUseCase: GetNotificationPreferencesUseCase,
    private val updateNotificationPreferencesUseCase: UpdateNotificationPreferencesUseCase
) :
    BaseEventViewModel<NotificationEvent, NotificationEffect>() {

    private val _state = MutableStateFlow(NotificationUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.Load -> load()
            is NotificationEvent.DismissDialog -> dismissDialog()

            is NotificationEvent.ToggleOrder -> toggleOrder(event.value)
            is NotificationEvent.TogglePromo -> togglePromo(event.value)
            is NotificationEvent.ToggleChat -> toggleChat(event.value)
            is NotificationEvent.TogglePush -> togglePush(event.value)
            is NotificationEvent.ToggleEmail -> toggleEmail(event.value)

            is NotificationEvent.ClickBack -> emitEffect(NotificationEffect.NavigateBack)
        }
    }

    private fun load() {
        observeDataFlow(
            flow = getNotificationPreferencesUseCase(),
            onState = { state ->
                _state.update {
                    if (state is LoadState.Success) {
                        state.data.toUiState()
                    } else {
                        it.copy(loading = state is LoadState.Loading)
                    }
                }
            },
            onError = ::showError
        )
    }

    private fun toggleOrder(value: Boolean) {
        persistPreference(uiState.value.copy(orderNotification = value))
    }

    private fun togglePromo(value: Boolean) {
        persistPreference(uiState.value.copy(promoNotification = value))
    }

    private fun toggleChat(value: Boolean) {
        persistPreference(uiState.value.copy(chatNotification = value))
    }

    private fun togglePush(value: Boolean) {
        persistPreference(uiState.value.copy(pushEnabled = value))
    }

    private fun toggleEmail(value: Boolean) {
        persistPreference(uiState.value.copy(emailEnabled = value))
    }

    private fun persistPreference(nextState: NotificationUiState) {
        _state.update { nextState.copy(loading = true) }

        observeDataFlow(
            flow = updateNotificationPreferencesUseCase(
                NotificationPreferencesParam(
                    orderNotification = nextState.orderNotification,
                    promoNotification = nextState.promoNotification,
                    chatNotification = nextState.chatNotification,
                    pushEnabled = nextState.pushEnabled,
                    emailEnabled = nextState.emailEnabled
                )
            ),
            onState = { state ->
                _state.update {
                    if (state is LoadState.Success) {
                        state.data.toUiState()
                    } else {
                        nextState.copy(loading = state is LoadState.Loading)
                    }
                }
            },
            onError = {
                load()
                showError(it)
            }
        )
    }

    private fun NotificationPreferences.toUiState() = NotificationUiState(
        loading = false,
        orderNotification = orderNotification,
        promoNotification = promoNotification,
        chatNotification = chatNotification,
        pushEnabled = pushEnabled,
        emailEnabled = emailEnabled
    }

    private fun showError(error: UiError) {
        setDialog(
            UiDialog.Center(
                state = DialogStateV1(
                    type = DialogType.ERROR,
                    title = ErrorMessages.GENERIC_ERROR,
                    message = error.message
                ),
                onPrimary = { dismissDialog() }
            )
        )
    }
}
