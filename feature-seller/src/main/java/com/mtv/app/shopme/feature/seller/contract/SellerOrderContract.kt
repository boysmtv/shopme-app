/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderContract.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 07.51
 */

package com.mtv.app.shopme.feature.seller.contract

data class SellerOrderUiState(
    val isLoading: Boolean = false,

    val selectedFilter: String = "All",
    val isOnline: Boolean = true,

    val orders: List<OrderSummary> = emptyList()
)

data class OrderSummary(
    val orderId: String,
    val customerName: String,
    val total: String,
    val date: String,
    val time: String,
    val paymentMethod: String,
    val status: String,
    val location: String
)

sealed class SellerOrderEvent {
    object Load : SellerOrderEvent()
    object DismissDialog : SellerOrderEvent()
    data class SelectFilter(val value: String) : SellerOrderEvent()
    object ToggleOnline : SellerOrderEvent()
    data class ClickOrder(val orderId: String) : SellerOrderEvent()
}

sealed class SellerOrderEffect {
    data class NavigateToOrderDetail(val orderId: String) : SellerOrderEffect()
}