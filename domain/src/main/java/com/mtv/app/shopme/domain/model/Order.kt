/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: Order.kt
 *
 * Last modified by Dedy Wijaya on 15/04/26 14.45
 */

package com.mtv.app.shopme.domain.model

import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class Order(
    val id: String = EMPTY_STRING,
    val customerId: String = EMPTY_STRING,
    val customerName: String = EMPTY_STRING,
    val cafeId: String = EMPTY_STRING,
    val cafeName: String = EMPTY_STRING,
    val items: List<OrderItem> = emptyList(),
    val itemCount: Int = 0,
    val totalPrice: Double = 0.0,
    val status: OrderStatus = OrderStatus.ORDERED,
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    val timestamp: Long = System.currentTimeMillis(),
    val createdAt: String = EMPTY_STRING,
    val deliveryAddress: String = EMPTY_STRING,
    val paymentMethod: PaymentMethod = PaymentMethod.TRANSFER,
    val timeline: List<OrderTimeline> = emptyList()
)
