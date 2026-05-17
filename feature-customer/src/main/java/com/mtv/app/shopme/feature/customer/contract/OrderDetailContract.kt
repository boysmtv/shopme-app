package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.domain.model.Order

data class OrderDetailUiState(
    val isLoading: Boolean = false,
    val orderId: String = "",
    val order: Order? = null,
    val showCancelDialog: Boolean = false,
    val cancelReason: String = ""
)

sealed class OrderDetailEvent {
    object Load : OrderDetailEvent()
    object DismissDialog : OrderDetailEvent()
    object ClickBack : OrderDetailEvent()
    object ClickChat : OrderDetailEvent()
    object ConfirmTransfer : OrderDetailEvent()
    object ClickCancelOrder : OrderDetailEvent()
    object SubmitCancelOrder : OrderDetailEvent()
    object DismissCancelDialog : OrderDetailEvent()
    data class ChangeCancelReason(val reason: String) : OrderDetailEvent()
}

sealed class OrderDetailEffect {
    object NavigateBack : OrderDetailEffect()
    data class NavigateToChat(val chatId: String) : OrderDetailEffect()
}
