/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderHistoryViewModel.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 09.58
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryDataListener
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryItem
import com.mtv.app.shopme.feature.customer.contract.OrderHistoryStateListener
import com.mtv.app.shopme.feature.customer.contract.OrderStatusFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OrderHistoryViewModel @Inject constructor() :
    BaseViewModel(),
    UiOwner<OrderHistoryStateListener, OrderHistoryDataListener> {

    override val uiState = MutableStateFlow(OrderHistoryStateListener())
    override val uiData = MutableStateFlow(OrderHistoryDataListener())

    init { loadOrders() }

    fun loadOrders() {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(loading = true)
            delay(800)

            uiData.value = uiData.value.copy(
                orders = listOf(
                    OrderHistoryItem("1", "Cafe Kopi Boy", "Cappuccino", "12 Feb 2026", "Rp 25.000", "SELESAI", 3, "E-Wallet", "Diantar"),
                    OrderHistoryItem("2", "Kopi Nusantara", "Caramel Latte", "11 Feb 2026", "Rp 32.000", "DIPROSES", 2, "QRIS", "Pickup"),
                    OrderHistoryItem("3", "Burger Town", "Cheese Burger Combo", "10 Feb 2026", "Rp 48.000", "DIKIRIM", 4, "Cash", "Diantar"),
                    OrderHistoryItem("4", "Coffee Corner", "Americano", "08 Feb 2026", "Rp 22.000", "SELESAI", 1, "E-Wallet", "Pickup"),
                    OrderHistoryItem("5", "Cafe Kopi Boy", "Matcha Latte", "05 Feb 2026", "Rp 30.000", "BATAL", 2, "QRIS", "Diantar")
                )
            )

            uiState.value = uiState.value.copy(loading = false)
        }
    }

    fun updateFilter(filter: OrderStatusFilter) {
        uiData.value = uiData.value.copy(selectedFilter = filter)
    }
}