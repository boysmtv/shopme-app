/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: Order.kt
 *
 * Last modified by Dedy Wijaya on 15/04/26 14.45
 */

package com.mtv.app.shopme.domain.model

data class Order(
    val id: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val cafeId: String = "",
    val cafeName: String = "",
    val items: List<OrderItem> = emptyList(),
    val itemCount: Int = 0,
    val totalPrice: Double = 0.0,
    val status: OrderStatus = OrderStatus.ORDERED,
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    val timestamp: Long = System.currentTimeMillis(),
    val createdAt: String = "",
    val deliveryAddress: String = "",
    val paymentMethod: PaymentMethod = PaymentMethod.TRANSFER,
    val transferConfirmationAvailable: Boolean = false,
    val timeline: List<OrderTimeline> = emptyList()
)
