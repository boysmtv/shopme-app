package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class SellerProfileResponse(
    val cafeId: String? = null,
    val sellerName: String,
    val sellerPhoto: String = "",
    val email: String,
    val phone: String,
    val storeName: String,
    val storePhoto: String = "",
    val storeAddress: String,
    val isOnline: Boolean,
    val hasCafe: Boolean
)

@Serializable
data class SellerOrderSummaryResponse(
    val orderId: String,
    val customerName: String,
    val total: String,
    val date: String,
    val time: String,
    val paymentMethod: String,
    val status: String,
    val location: String
)
