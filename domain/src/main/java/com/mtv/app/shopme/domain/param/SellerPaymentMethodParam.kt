package com.mtv.app.shopme.domain.param

data class SellerPaymentMethodParam(
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
