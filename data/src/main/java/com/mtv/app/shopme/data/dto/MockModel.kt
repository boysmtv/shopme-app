/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodItemModel.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 15.29
 */

package com.mtv.app.shopme.data.dto

import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING


data class OrderItemModel(
    val foodId: Int = 0,
    val quantity: Int = 0,
    val price: Double = 0.0
)

enum class PaymentMethod {
    CASH,
    TRANSFER
}
data class OrderModel(
    val id: String = EMPTY_STRING,
    val customerId: String = EMPTY_STRING,
    val cafeId: String = EMPTY_STRING,
    val items: List<OrderItemModel> = emptyList(),
    val totalPrice: Double = 0.0,
    val status: OrderStatus = OrderStatus.ORDERED,
    val timestamp: Long = System.currentTimeMillis(),
    val deliveryAddress: String = EMPTY_STRING,
    val paymentMethod: PaymentMethod = PaymentMethod.TRANSFER
)

enum class OrderStatus(val value: String) {
    ORDERED("ORDERED"),
    COOKING("COOKING"),
    DELIVERING("DELIVERING"),
    COMPLETED("COMPLETED");

    companion object {
        fun fromValue(value: String): OrderStatus {
            return OrderStatus.entries.find { it.value == value.uppercase() } ?: ORDERED
        }
    }
}