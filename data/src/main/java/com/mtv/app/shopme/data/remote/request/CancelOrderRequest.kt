package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class CancelOrderRequest(
    val reason: String? = null
)
