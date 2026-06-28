/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderViewModel.kt
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeEventType
import com.mtv.app.shopme.core.realtime.ShopmeRealtimeGateway
import com.mtv.app.shopme.domain.usecase.ConfirmOrderTransferUseCase
import com.mtv.app.shopme.domain.usecase.EnsureOrderChatConversationUseCase
import com.mtv.app.shopme.domain.usecase.GetOrdersUseCase
import com.mtv.app.shopme.feature.customer.contract.OrderEffect
import com.mtv.app.shopme.feature.customer.contract.OrderEvent
import com.mtv.app.shopme.feature.customer.contract.OrderUiState
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
class OrderViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val confirmOrderTransferUseCase: ConfirmOrderTransferUseCase,
    private val ensureOrderChatConversationUseCase: EnsureOrderChatConversationUseCase,
    private val realtimeGateway: ShopmeRealtimeGateway,
    private val sessionManager: SessionManager
) : BaseEventViewModel<OrderEvent, OrderEffect>() {

    private val _state = MutableStateFlow(OrderUiState())
    val uiState = _state.asStateFlow()
    private var realtimeRetained = false

    init {
        viewModelScope.launch {
            realtimeGateway.events.collectLatest { event ->
                if (event.type == ShopmeRealtimeEventType.NOTIFICATION_CREATED) {
                    loadOrders()
                }
            }
        }
    }

    override fun onEvent(event: OrderEvent) {
        when (event) {
            is OrderEvent.Load -> loadOrders()
            is OrderEvent.DismissDialog -> dismissDialog()
            is OrderEvent.Reload -> loadOrders()
            is OrderEvent.LoadMore -> loadMoreOrders()
            is OrderEvent.ClickOrder -> emitEffect(OrderEffect.NavigateToDetail(event.orderId))
            is OrderEvent.ConfirmTransfer -> confirmTransfer(event.orderId)
            is OrderEvent.SelectFilter -> _state.update { it.copy(selectedFilter = event.filter) }
            is OrderEvent.ClickChatList -> emitEffect(OrderEffect.NavigateToChatList)
            is OrderEvent.ClickChat -> openChat(event.orderId)
            is OrderEvent.ClickBack -> emitEffect(OrderEffect.NavigateBack)
        }
    }

    private fun openChat(orderId: String) {
        if (orderId.isBlank()) return
        observeDataFlow(
            flow = ensureOrderChatConversationUseCase(orderId),
            onSuccess = { emitEffect(OrderEffect.NavigateToChat(it)) },
            onError = { error ->
                _state.update { it.copy(isLoading = false) }
                showError(error, onRetry = { openChat(orderId) })
            }
        )
    }

    private fun loadOrders() {
        retainRealtime()
        observeDataFlow(
            flow = getOrdersUseCase(0, PAGE_SIZE),
            onState = { result ->
                _state.update {
                    it.copy(
                        isLoading = result is LoadState.Loading && it.orders.isEmpty(),
                        isRefreshing = result is LoadState.Loading && it.orders.isNotEmpty(),
                        isLoadingMore = false,
                        currentPage = if (result is LoadState.Success) result.data.page else it.currentPage,
                        isLastPage = if (result is LoadState.Success) result.data.last else it.isLastPage,
                        orders = if (result is LoadState.Success) result.data.content else it.orders
                    )
                }
            },
            onError = { error ->
                _state.update { it.copy(isLoading = false, isRefreshing = false, isLoadingMore = false) }
                showError(error)
            }
        )
    }

    private fun loadMoreOrders() {
        val state = _state.value
        if (state.isLoading || state.isRefreshing || state.isLoadingMore || state.isLastPage) return
        observeIndependentDataFlow(
            flow = getOrdersUseCase(state.currentPage + 1, PAGE_SIZE),
            onState = { result ->
                _state.update {
                    when (result) {
                        is LoadState.Loading -> it.copy(isLoadingMore = true)
                        is LoadState.Success -> it.copy(
                            isLoadingMore = false,
                            currentPage = result.data.page,
                            isLastPage = result.data.last,
                            orders = it.orders + result.data.content
                        )
                        is LoadState.Error -> it.copy(isLoadingMore = false)
                        else -> it
                    }
                }
            },
            onError = { error ->
                _state.update { it.copy(isLoadingMore = false) }
                showError(error)
            }
        )
    }

    private fun confirmTransfer(orderId: String) {
        observeDataFlow(
            flow = confirmOrderTransferUseCase(orderId),
            onState = { state ->
                _state.update { it.copy(isLoading = state is LoadState.Loading) }
            },
            onSuccess = { loadOrders() },
            onError = { error ->
                _state.update { it.copy(isLoading = false) }
                showError(error)
            }
        )
    }

    private fun showError(error: UiError, onRetry: (() -> Unit)? = null) {
        handleSessionError(
            error = error,
            sessionManager = sessionManager,
            beforeLogout = { _state.update { it.copy(isLoading = false) } }
        ) {
            setDialog(
                UiDialog.Center(
                    state = DialogStateV1(
                        type = DialogType.ERROR,
                        title = ErrorMessages.GENERIC_ERROR,
                        message = it.message,
                        secondaryButtonText = if (onRetry != null) "Coba Lagi" else null
                    ),
                    onPrimary = { dismissDialog() },
                    onSecondary = if (onRetry != null) { { dismissDialog(); onRetry() } } else null
                )
            )
        }
    }

    private fun retainRealtime() {
        if (realtimeRetained) return
        realtimeRetained = true
        realtimeGateway.retain()
    }

    override fun onCleared() {
        if (realtimeRetained) {
            realtimeGateway.release()
        }
        super.onCleared()
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}
