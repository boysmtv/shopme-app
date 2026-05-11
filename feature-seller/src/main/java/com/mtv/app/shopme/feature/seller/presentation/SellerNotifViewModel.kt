/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerNotifViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.27
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.usecase.ClearNotificationsUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerNotificationsUseCase
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.based.core.network.utils.ResourceFirebase
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SellerNotifViewModel @Inject constructor(
    private val getSellerNotificationsUseCase: GetSellerNotificationsUseCase,
    private val clearNotificationsUseCase: ClearNotificationsUseCase,
    private val sessionManager: SessionManager,
) : BaseEventViewModel<SellerNotifEvent, SellerNotifEffect>() {

    private val _state = MutableStateFlow(SellerNotifUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerNotifEvent) {
        when (event) {
            SellerNotifEvent.Load, SellerNotifEvent.GetNotification -> getNotifications()
            SellerNotifEvent.DismissDialog -> dismissDialog()
            SellerNotifEvent.ClearNotification -> clearNotifications()
            is SellerNotifEvent.ClickNotification -> Unit
            SellerNotifEvent.ClickBack -> emitEffect(SellerNotifEffect.NavigateBack)
        }
    }

    private fun getNotifications() {
        observeDataFlow(
            flow = getSellerNotificationsUseCase(),
            onState = { state ->
                _state.update {
                    it.copy(
                        notifications = if (state is com.mtv.based.core.network.utils.LoadState.Success) state.data else it.notifications,
                        notificationState = when (state) {
                            is com.mtv.based.core.network.utils.LoadState.Loading -> ResourceFirebase.Loading
                            is com.mtv.based.core.network.utils.LoadState.Success -> ResourceFirebase.Success("")
                            is com.mtv.based.core.network.utils.LoadState.Error -> ResourceFirebase.Success("")
                            else -> ResourceFirebase.Success("")
                        }
                    )
                }
            },
            onError = ::showError
        )
    }

    private fun clearNotifications() {
        observeDataFlow(
            flow = clearNotificationsUseCase(),
            onState = { state ->
                _state.update {
                    it.copy(
                        notifications = if (state is com.mtv.based.core.network.utils.LoadState.Success) emptyList() else it.notifications,
                        activeDialog = if (state is com.mtv.based.core.network.utils.LoadState.Success) SellerNotifDialog.Success else it.activeDialog,
                        notificationState = when (state) {
                            is com.mtv.based.core.network.utils.LoadState.Loading -> ResourceFirebase.Loading
                            is com.mtv.based.core.network.utils.LoadState.Success -> ResourceFirebase.Success("")
                            is com.mtv.based.core.network.utils.LoadState.Error -> ResourceFirebase.Success("")
                            else -> ResourceFirebase.Success("")
                        }
                    )
                }
            },
            onError = ::showError
        )
    }

    private fun showError(error: UiError) {
        handleSessionError(
            error = error,
            sessionManager = sessionManager,
            beforeLogout = {
                _state.update {
                    it.copy(notificationState = ResourceFirebase.Success(""), activeDialog = null)
                }
            }
        ) { uiError ->
            _state.update {
                it.copy(
                    activeDialog = SellerNotifDialog.Error(uiError.message),
                    notificationState = ResourceFirebase.Success("")
                )
            }
        }
    }
}
