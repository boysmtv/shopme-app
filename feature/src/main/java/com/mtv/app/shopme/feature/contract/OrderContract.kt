/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderContract.kt
 */

package com.mtv.app.shopme.feature.contract

import com.mtv.app.shopme.data.OrderModel

data class OrderStateListener(
    val isLoading: Boolean = false,
    val activeDialog: OrderDialog? = null
)

data class OrderDataListener(
    val orders: List<OrderModel> = emptyList()
)

data class OrderEventListener(
    val onReload: () -> Unit = {},
    val onOrderClick: (String) -> Unit = {}
)

data class OrderNavigationListener(
    val onDetail: (String) -> Unit = {},
    val onBack: () -> Unit = {}
)

sealed class OrderDialog {
    object None : OrderDialog()
}
