/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderViewModel.kt
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.data.dto.OrderItemModel
import com.mtv.app.shopme.data.dto.OrderModel
import com.mtv.app.shopme.data.dto.OrderStatus
import com.mtv.app.shopme.feature.customer.contract.OrderEffect
import com.mtv.app.shopme.feature.customer.contract.OrderEvent
import com.mtv.app.shopme.feature.customer.contract.OrderUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OrderViewModel @Inject constructor() :
    BaseEventViewModel<OrderEvent, OrderEffect>() {

    private val _state = MutableStateFlow(OrderUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: OrderEvent) {
        when (event) {
            is OrderEvent.Load -> loadOrders()
            is OrderEvent.DismissDialog -> dismissDialog()
            is OrderEvent.Reload -> loadOrders()
            is OrderEvent.ClickOrder -> emitEffect(OrderEffect.NavigateToDetail(event.orderId))
            is OrderEvent.ClickChat -> emitEffect(OrderEffect.NavigateToChat)
            is OrderEvent.ClickBack -> emitEffect(OrderEffect.NavigateBack)
        }
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            delay(800)

            val dummy = listOf(
                OrderModel(
                    id = "A001",
                    customerId = "C001",
                    cafeId = "Mamah Al Cafe",
                    items = listOf(
                        OrderItemModel(foodId = 0, quantity = 2, price = 15000.0),
                        OrderItemModel(foodId = 3, quantity = 1, price = 18000.0)
                    ),
                    totalPrice = 48000.0,
                    status = OrderStatus.COOKING
                ),
                OrderModel(
                    id = "A002",
                    customerId = "C001",
                    cafeId = "Mamah Al Cafe",
                    items = listOf(
                        OrderItemModel(foodId = 1, quantity = 1, price = 30000.0),
                        OrderItemModel(foodId = 4, quantity = 2, price = 20000.0)
                    ),
                    totalPrice = 70000.0,
                    status = OrderStatus.DELIVERING
                ),
                OrderModel(
                    id = "A003",
                    customerId = "C001",
                    cafeId = "Mamah Al Cafe",
                    items = listOf(
                        OrderItemModel(foodId = 2, quantity = 3, price = 12000.0),
                        OrderItemModel(foodId = 5, quantity = 1, price = 16000.0)
                    ),
                    totalPrice = 52000.0,
                    status = OrderStatus.COMPLETED
                ),
                OrderModel(
                    id = "A004",
                    customerId = "C001",
                    cafeId = "Mamah Al Cafe",
                    items = listOf(
                        OrderItemModel(foodId = 6, quantity = 1, price = 25000.0),
                        OrderItemModel(foodId = 7, quantity = 2, price = 22000.0)
                    ),
                    totalPrice = 69000.0,
                    status = OrderStatus.ORDERED
                )
            )

            _state.update {
                it.copy(
                    isLoading = false,
                    orders = dummy
                )
            }
        }
    }

    private fun showError(error: UiError) {
        setDialog(
            UiDialog.Center(
                state = DialogStateV1(
                    type = DialogType.ERROR,
                    title = ErrorMessages.GENERIC_ERROR,
                    message = error.message
                ),
                onPrimary = { dismissDialog() }
            )
        )
    }
}