package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscountRequest(
    val name: String,
    val type: String,
    val value: String,
    @SerialName("minOrder") val minOrder: String? = null,
    @SerialName("maxDiscount") val maxDiscount: String? = null,
    @SerialName("startDate") val startDate: String,
    @SerialName("endDate") val endDate: String,
    @SerialName("isActive") val isActive: Boolean = true
)
