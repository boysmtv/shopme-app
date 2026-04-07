/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderHistoryContract.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 09.56
 */

package com.mtv.app.shopme.feature.customer.contract

enum class OrderStatusFilter {
    SEMUA, SELESAI, DIPROSES, BATAL
}

data class OrderHistoryUiState(
    val loading: Boolean = false,

    val selectedFilter: OrderStatusFilter = OrderStatusFilter.SEMUA,
    val orders: List<OrderHistoryItem> = emptyList()
)

sealed class OrderHistoryEvent {
    object Load : OrderHistoryEvent()
    object DismissDialog : OrderHistoryEvent()

    object Refresh : OrderHistoryEvent()
    data class ChangeFilter(val filter: OrderStatusFilter) : OrderHistoryEvent()
    data class ClickOrder(val item: OrderHistoryItem) : OrderHistoryEvent()

    object ClickBack : OrderHistoryEvent()
}

sealed class OrderHistoryEffect {
    object NavigateBack : OrderHistoryEffect()
    data class NavigateToDetail(val item: OrderHistoryItem) : OrderHistoryEffect()
}

data class OrderHistoryItem(
    val id: String,
    val storeName: String,
    val title: String,
    val date: String,
    val price: String,
    val status: String,
    val totalItems: Int,
    val paymentMethod: String,
    val deliveryType: String
)