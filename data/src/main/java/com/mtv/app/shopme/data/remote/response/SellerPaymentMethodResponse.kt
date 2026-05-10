package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class SellerPaymentMethodResponse(
    val cashEnabled: Boolean,
    val bankEnabled: Boolean,
    val bankNumber: String? = null,
    val ovoEnabled: Boolean,
    val ovoNumber: String? = null,
    val danaEnabled: Boolean,
    val danaNumber: String? = null,
    val gopayEnabled: Boolean,
    val gopayNumber: String? = null
)
