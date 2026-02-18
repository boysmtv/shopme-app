/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderViewModel.kt
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.data.OrderItemModel
import com.mtv.app.shopme.data.OrderModel
import com.mtv.app.shopme.data.OrderStatus
import com.mtv.app.shopme.feature.customer.contract.OrderDataListener
import com.mtv.app.shopme.feature.customer.contract.OrderStateListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderViewModel @Inject constructor() :
    BaseViewModel(),
    UiOwner<OrderStateListener, OrderDataListener> {

    override val uiState = MutableStateFlow(OrderStateListener())
    override val uiData = MutableStateFlow(OrderDataListener())

    init {
        loadOrders()
    }

    fun loadOrders() = viewModelScope.launch {
        uiState.value = uiState.value.copy(isLoading = true)

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

        uiData.value = uiData.value.copy(orders = dummy)
        uiState.value = uiState.value.copy(isLoading = false)
    }

    fun selectOrder(orderId: String) {

    }

    fun dismissDialog() {
        uiState.value = uiState.value.copy(activeDialog = null)
    }
}
