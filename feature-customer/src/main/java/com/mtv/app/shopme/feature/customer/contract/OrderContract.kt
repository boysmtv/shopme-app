/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.data.OrderModel

data class OrderStateListener(
    val isLoading: Boolean = false,
    val activeDialog: com.mtv.app.shopme.feature.customer.contract.OrderDialog? = null
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
    val onChatClick: () -> Unit = {},
    val onBack: () -> Unit = {},
)

sealed class OrderDialog {
    object None : OrderDialog()
}
