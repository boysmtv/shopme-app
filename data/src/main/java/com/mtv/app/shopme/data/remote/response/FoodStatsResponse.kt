package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FoodStatsResponse(
    @SerialName("cafeId") val cafeId: String = "",
    @SerialName("totalProducts") val totalProducts: Long = 0,
    @SerialName("activeProducts") val activeProducts: Long = 0,
    @SerialName("inactiveProducts") val inactiveProducts: Long = 0,
    @SerialName("readyProducts") val readyProducts: Long = 0,
    @SerialName("jastipProducts") val jastipProducts: Long = 0,
    @SerialName("preorderProducts") val preorderProducts: Long = 0,
    @SerialName("totalStock") val totalStock: Long = 0,
    @SerialName("totalSold") val totalSold: Long = 0
)
