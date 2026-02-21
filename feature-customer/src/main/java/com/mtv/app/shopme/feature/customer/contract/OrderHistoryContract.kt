/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderHistoryContract.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 09.56
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.based.core.network.utils.ResourceFirebase

enum class OrderStatusFilter {
    SEMUA, SELESAI, DIPROSES, BATAL
}

data class OrderHistoryStateListener(
    val loading: Boolean = false
)

data class OrderHistoryDataListener(
    val selectedFilter: OrderStatusFilter = OrderStatusFilter.SEMUA,
    val orders: List<OrderHistoryItem> = emptyList()
)

data class OrderHistoryEventListener(
    val onRefresh: () -> Unit = {},
    val onFilterChange: (OrderStatusFilter) -> Unit = {},
    val onClickOrder: (OrderHistoryItem) -> Unit = {}
)

data class OrderHistoryNavigationListener(
    val onBack: () -> Unit = {},
    val onDetail: (OrderHistoryItem) -> Unit = {}
)

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