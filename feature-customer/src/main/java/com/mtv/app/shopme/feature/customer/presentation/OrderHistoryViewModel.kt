/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderHistoryViewModel.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 09.58
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryEffect
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryEvent
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryItem
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryUiState
import com.mtv.app.shopme.feature.customer.contract.OrderStatusFilter
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OrderHistoryViewModel @Inject constructor() :
    BaseEventViewModel<OrderHistoryEvent, OrderHistoryEffect>() {

    private val _state = MutableStateFlow(OrderHistoryUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: OrderHistoryEvent) {
        when (event) {
            is OrderHistoryEvent.Load -> loadOrders()
            is OrderHistoryEvent.DismissDialog -> dismissDialog()

            is OrderHistoryEvent.Refresh -> loadOrders()
            is OrderHistoryEvent.ChangeFilter -> changeFilter(event.filter)
            is OrderHistoryEvent.ClickOrder -> {
                emitEffect(OrderHistoryEffect.NavigateToDetail(event.item))
            }

            is OrderHistoryEvent.ClickBack -> emitEffect(OrderHistoryEffect.NavigateBack)
        }
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            delay(800)

            val data = listOf(
                OrderHistoryItem("1", "Cafe Kopi Boy", "Cappuccino", "12 Feb 2026", "Rp 25.000", "SELESAI", 3, "E-Wallet", "Diantar"),
                OrderHistoryItem("2", "Kopi Nusantara", "Caramel Latte", "11 Feb 2026", "Rp 32.000", "DIPROSES", 2, "QRIS", "Pickup"),
                OrderHistoryItem("3", "Burger Town", "Cheese Burger Combo", "10 Feb 2026", "Rp 48.000", "DIKIRIM", 4, "Cash", "Diantar"),
                OrderHistoryItem("4", "Coffee Corner", "Americano", "08 Feb 2026", "Rp 22.000", "SELESAI", 1, "E-Wallet", "Pickup"),
                OrderHistoryItem("5", "Cafe Kopi Boy", "Matcha Latte", "05 Feb 2026", "Rp 30.000", "BATAL", 2, "QRIS", "Diantar")
            )

            _state.update {
                it.copy(
                    loading = false,
                    orders = data
                )
            }
        }
    }

    private fun changeFilter(filter: OrderStatusFilter) {
        _state.update {
            it.copy(selectedFilter = filter)
        }
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