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
import com.mtv.app.shopme.feature.customer.contract.*
import com.mtv.based.core.network.utils.ResourceFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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
                    OrderHistoryItem("1", "Cappuccino", "12 Feb 2026", "Rp 25.000", "SELESAI"),
                    OrderHistoryItem("2", "Latte", "10 Feb 2026", "Rp 28.000", "DIPROSES"),
                    OrderHistoryItem("3", "Burger", "5 Feb 2026", "Rp 45.000", "BATAL")
                )
            )

            uiState.value = uiState.value.copy(loading = false)
        }
    }

    fun updateFilter(filter: OrderStatusFilter) {
        uiData.value = uiData.value.copy(selectedFilter = filter)
    }
}