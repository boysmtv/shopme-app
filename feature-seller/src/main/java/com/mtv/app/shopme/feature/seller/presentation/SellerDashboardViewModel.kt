/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerDashboardViewModel.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.15
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEventType
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeGateway
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerOrdersUseCase
import com.mtv.app.shopme.domain.usecase.GetUnreadNotificationCountUseCase
import com.mtv.app.shopme.domain.usecase.UpdateSellerAvailabilityUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEffect
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEffect.*
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEvent
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SessionManager
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
class SellerDashboardViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val getSellerOrdersUseCase: GetSellerOrdersUseCase,
    private val getSellerProfileUseCase: GetSellerProfileUseCase,
    private val getUnreadNotificationCountUseCase: GetUnreadNotificationCountUseCase,
    private val updateSellerAvailabilityUseCase: UpdateSellerAvailabilityUseCase,
    private val realtimeGateway: ShopmeRealtimeGateway
) : BaseEventViewModel<SellerDashboardEvent, SellerDashboardEffect>() {

    private val _state = MutableStateFlow(SellerDashboardUiState())
    val uiState = _state.asStateFlow()

    init {
        viewModelScope.launch {
            realtimeGateway.events.collectLatest { event ->
                if (event.type == ShopmeRealtimeEventType.NOTIFICATION_CREATED) {
                    observeNotifications()
                    refresh()
                }
            }
        }
    }

    override fun onEvent(event: SellerDashboardEvent) {
        when (event) {
            SellerDashboardEvent.Load -> load()
            SellerDashboardEvent.DismissDialog -> dismissDialog()
            SellerDashboardEvent.Refresh -> refresh()
            SellerDashboardEvent.ClickProduct ->
                emitEffect(SellerDashboardEffect.NavigateToProduct)

            SellerDashboardEvent.ClickOrder ->
                emitEffect(SellerDashboardEffect.NavigateToOrder)

            is SellerDashboardEvent.ClickOrderDetail ->
                emitEffect(NavigateToOrderDetail(event.orderId))

            SellerDashboardEvent.ClickNotif ->
                emitEffect(SellerDashboardEffect.NavigateToNotif)

            is SellerDashboardEvent.ChangeFilter ->
                _state.update { it.copy(selectedFilter = event.value) }

            is SellerDashboardEvent.ChangeSort ->
                _state.update { it.copy(selectedSort = event.value) }

            SellerDashboardEvent.ToggleOnline ->
                toggleOnline()
        }
    }

    private fun load() {
        realtimeGateway.ensureConnected()
        observeProfile()
        observeNotifications()
        refresh()
    }

    private fun observeProfile() {
        observeIndependentDataFlow(
            flow = getSellerProfileUseCase(),
            onState = { result ->
                _state.update {
                    val profile = (result as? LoadState.Success)?.data
                    it.copy(
                        isOnline = profile?.isOnline ?: it.isOnline,
                        storeName = profile?.storeName ?: it.storeName,
                        storeAddress = profile?.storeAddress ?: it.storeAddress
                    )
                }
            },
            onError = ::showError
        )
    }

    private fun observeNotifications() {
        observeIndependentDataFlow(
            flow = getUnreadNotificationCountUseCase(),
            onState = { result ->
                _state.update {
                    it.copy(
                        notificationCount = (result as? LoadState.Success)?.data ?: it.notificationCount
                    )
                }
            },
            onError = ::showError
        )
    }

    private fun refresh() {
        observeIndependentDataFlow(
            flow = getSellerOrdersUseCase(),
            onState = { result ->
                _state.update {
                    it.copy(
                        isLoading = result is LoadState.Loading,
                        isRefreshing = result is LoadState.Loading && it.orders.isNotEmpty(),
                        orders = if (result is LoadState.Success) result.data else it.orders,
                        errorMessage = when (result) {
                            is LoadState.Success, is LoadState.Loading -> null
                            else -> it.errorMessage
                        }
                    )
                }
            },
            onError = {
                _state.update { state ->
                    state.copy(isLoading = false, isRefreshing = false, errorMessage = it.message)
                }
                showError(it)
            }
        )
    }

    private fun toggleOnline() {
        observeDataFlow(
            flow = updateSellerAvailabilityUseCase(!_state.value.isOnline),
            onState = { result ->
                _state.update {
                    val profile = (result as? LoadState.Success)?.data
                    it.copy(
                        isLoading = result is LoadState.Loading || it.isLoading,
                        isOnline = profile?.isOnline ?: it.isOnline
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
            beforeLogout = { _state.update { it.copy(isLoading = false, isRefreshing = false) } }
        ) {
            setDialog(
                UiDialog.Center(
                    state = DialogStateV1(
                        type = DialogType.ERROR,
                        title = ErrorMessages.GENERIC_ERROR,
                        message = it.message
                    ),
                    onPrimary = { dismissDialog() }
                )
            )
        }
    }
}
