package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SellerCategoryResponse(
    val id: String,
    @SerialName("cafeId") val cafeId: String,
    val name: String,
    @SerialName("isActive") val isActive: Boolean,
    @SerialName("createdAt") val createdAt: String
)
