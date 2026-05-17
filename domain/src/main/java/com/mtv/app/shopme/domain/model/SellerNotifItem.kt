package com.mtv.app.shopme.domain.model

data class SellerNotifItem(
    val title: String,
    val message: String,
    val orderId: String,
    val buyerName: String,
    val date: String,
    val time: String,
    val isRead: Boolean,
    val orderItemsSummary: String = "",
    val deliveryAddress: String = "",
    val paymentStatus: String = "",
    val orderStatus: String = "",
)
