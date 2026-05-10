/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.domain.model.Order

data class OrderUiState(
    val isLoading: Boolean = false,
    val orders: List<Order> = emptyList(),
    val activeDialog: OrderDialog? = null
)

sealed class OrderEvent {
    object Load : OrderEvent()
    object DismissDialog : OrderEvent()

    object Reload : OrderEvent()
    data class ClickOrder(val orderId: String) : OrderEvent()
    data class ConfirmTransfer(val orderId: String) : OrderEvent()

    object ClickBack : OrderEvent()
    object ClickChat : OrderEvent()
}

sealed class OrderEffect {
    object NavigateBack : OrderEffect()
    object NavigateToChat : OrderEffect()
    data class NavigateToDetail(val orderId: String) : OrderEffect()
}

sealed class OrderDialog {
    object None : OrderDialog()
}
