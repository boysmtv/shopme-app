package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class SellerPaymentMethodRequest(
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
