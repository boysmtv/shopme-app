package com.mtv.app.shopme.domain.model

data class SellerPaymentMethod(
    val cashEnabled: Boolean,
    val bankEnabled: Boolean,
    val bankNumber: String,
    val ovoEnabled: Boolean,
    val ovoNumber: String,
    val danaEnabled: Boolean,
    val danaNumber: String,
    val gopayEnabled: Boolean,
    val gopayNumber: String
)
