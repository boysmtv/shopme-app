/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderDetailContract.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 13.04
 */

package com.mtv.app.shopme.feature.seller.contract

import androidx.compose.runtime.Immutable
import com.mtv.app.shopme.domain.model.OrderStatus

data class SellerOrderLineItem(
    val title: String,
    val qty: Int,
    val price: String,
    val notes: String = ""
)

@Immutable
data class SellerOrderDetailUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val orderId: String = "",
    val currentStatus: OrderStatus = OrderStatus.ORDERED,
    val customerName: String = "",
    val customerAddress: String = "",
    val paymentMethod: String = "",
    val paymentStatus: String = "",
    val total: String = "",
    val items: List<SellerOrderLineItem> = emptyList(),
    val showCancelDialog: Boolean = false,
    val cancelReason: String = "",
    val errorMessage: String? = null
)

sealed class SellerOrderDetailEvent {
    object Load : SellerOrderDetailEvent()
    object DismissDialog : SellerOrderDetailEvent()
    data class ChangeStatus(val status: OrderStatus) : SellerOrderDetailEvent()
    object SaveStatus : SellerOrderDetailEvent()
    object ClickCancelOrder : SellerOrderDetailEvent()
    object SubmitCancelOrder : SellerOrderDetailEvent()
    object DismissCancelDialog : SellerOrderDetailEvent()
    data class ChangeCancelReason(val reason: String) : SellerOrderDetailEvent()
    object ClickChat : SellerOrderDetailEvent()
    object ClickBack : SellerOrderDetailEvent()
}

sealed class SellerOrderDetailEffect {
    object NavigateBack : SellerOrderDetailEffect()
    object UpdateSuccess : SellerOrderDetailEffect()
    data class NavigateToChat(val chatId: String) : SellerOrderDetailEffect()
}
