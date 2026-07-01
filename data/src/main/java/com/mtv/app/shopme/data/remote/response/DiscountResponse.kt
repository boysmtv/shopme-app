package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscountResponse(
    val id: String,
    @SerialName("cafeId") val cafeId: String,
    val name: String,
    val type: String,
    val value: String,
    @SerialName("minOrder") val minOrder: String? = null,
    @SerialName("maxDiscount") val maxDiscount: String? = null,
    @SerialName("startDate") val startDate: String,
    @SerialName("endDate") val endDate: String,
    @SerialName("isActive") val isActive: Boolean,
    @SerialName("createdAt") val createdAt: String
)
