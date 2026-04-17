/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderViewModel.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 13.21
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.based.core.provider.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SellerOrderViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : BaseEventViewModel<SellerOrderEvent, SellerOrderEffect>() {

    private val _state = MutableStateFlow(SellerOrderUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerOrderEvent) {
        when (event) {

            SellerOrderEvent.Load -> load()

            SellerOrderEvent.DismissDialog -> dismissDialog()

            is SellerOrderEvent.SelectFilter ->
                update { copy(selectedFilter = event.value) }

            SellerOrderEvent.ToggleOnline ->
                update { copy(isOnline = !isOnline) }

            is SellerOrderEvent.ClickOrder ->
                emitEffect(SellerOrderEffect.NavigateToOrderDetail(event.orderId))
        }
    }

    private fun update(block: SellerOrderUiState.() -> SellerOrderUiState) {
        _state.update { it.block() }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            delay(1000) // simulate API

            _state.update {
                it.copy(
                    isLoading = false,
                    orders = dummyOrders()
                )
            }
        }
    }

    private fun dummyOrders(): List<OrderSummary> {
        return listOf(
            OrderSummary(
                orderId = "1",
                customerName = "John Doe",
                total = "50000",
                date = "2026-02-18",
                time = "12:30",
                paymentMethod = "Cash",
                status = "ORDERED",
                location = "Jakarta"
            ),
            OrderSummary(
                orderId = "2",
                customerName = "Jane Doe",
                total = "75000",
                date = "2026-02-18",
                time = "13:00",
                paymentMethod = "QRIS",
                status = "PROCESS",
                location = "Bandung"
            )
        )
    }
}