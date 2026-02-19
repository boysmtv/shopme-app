/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderContract.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 07.51
 */

package com.mtv.app.shopme.feature.seller.contract

class SellerOrderStateListener(
    var selectedFilter: String = "All",
    var isOnline: Boolean = true
)

class SellerOrderDataListener(
    var orders: List<OrderSummary> = emptyList()
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

class SellerOrderEventListener(
    val onToggleOnline: () -> Unit,
    val onSelectFilter: (String) -> Unit
)

class SellerOrderNavigationListener(
    val onNavigateToOrderDetail: () -> Unit
)
