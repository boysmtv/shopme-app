/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderHistoryViewModel.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 09.58
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.usecase.GetOrdersUseCase
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryEffect
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryEvent
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryItem
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryUiState
import com.mtv.app.shopme.feature.customer.contract.OrderStatusFilter
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
import kotlinx.coroutines.flow.update

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val sessionManager: SessionManager
) : BaseEventViewModel<OrderHistoryEvent, OrderHistoryEffect>() {

    private val _state = MutableStateFlow(OrderHistoryUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: OrderHistoryEvent) {
        when (event) {
            is OrderHistoryEvent.Load -> loadOrders()
            is OrderHistoryEvent.DismissDialog -> dismissDialog()
            is OrderHistoryEvent.Refresh -> loadOrders()
            is OrderHistoryEvent.ChangeFilter -> _state.update { it.copy(selectedFilter = event.filter) }
            is OrderHistoryEvent.ClickOrder -> emitEffect(OrderHistoryEffect.NavigateToDetail(event.item))
            is OrderHistoryEvent.ClickBack -> emitEffect(OrderHistoryEffect.NavigateBack)
        }
    }

    private fun loadOrders() {
        observeDataFlow(
            flow = getOrdersUseCase(),
            onState = { state ->
                _state.update {
                    it.copy(
                        loading = state is LoadState.Loading,
                        orders = if (state is LoadState.Success) state.data.map(::toItem) else it.orders
                    )
                }
            },
            onError = ::showError
        )
    }

    private fun toItem(order: Order): OrderHistoryItem = OrderHistoryItem(
        id = order.id,
        storeName = order.cafeName.ifBlank { order.cafeId },
        title = order.items.firstOrNull()?.foodName?.ifBlank { order.items.firstOrNull()?.foodId.orEmpty() }.orEmpty().ifBlank { "Order ${order.id}" },
        date = order.createdAt.substringBefore("T").ifBlank { "-" },
        price = "Rp ${order.totalPrice.toInt()}",
        status = when (order.status) {
            OrderStatus.COMPLETED -> "SELESAI"
            OrderStatus.CANCELLED -> "BATAL"
            else -> "DIPROSES"
        },
        totalItems = order.items.sumOf { it.quantity },
        paymentMethod = order.paymentMethod.name,
        deliveryType = "Delivery"
    )

    private fun showError(error: UiError) {
        handleSessionError(
            error = error,
            sessionManager = sessionManager,
            beforeLogout = { _state.update { it.copy(loading = false) } }
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
