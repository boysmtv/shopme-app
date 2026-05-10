package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class SellerAvailabilityRequest(
    val isOnline: Boolean
)
