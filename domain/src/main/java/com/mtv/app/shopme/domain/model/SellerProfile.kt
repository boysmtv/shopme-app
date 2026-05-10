package com.mtv.app.shopme.domain.model

data class SellerProfile(
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
