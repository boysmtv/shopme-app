package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.domain.model.Order

data class OrderDetailUiState(
    val isLoading: Boolean = false,
    val orderId: String = "",
    val order: Order? = null
)

sealed class OrderDetailEvent {
    object Load : OrderDetailEvent()
    object DismissDialog : OrderDetailEvent()
    object ClickBack : OrderDetailEvent()
    object ClickChat : OrderDetailEvent()
    object ConfirmTransfer : OrderDetailEvent()
}

sealed class OrderDetailEffect {
    object NavigateBack : OrderDetailEffect()
    object NavigateToChat : OrderDetailEffect()
}
