package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    val id: String,
    @SerialName("foodId") val foodId: String,
    @SerialName("foodName") val foodName: String? = null,
    @SerialName("cafeId") val cafeId: String,
    @SerialName("customerName") val customerName: String? = null,
    @SerialName("customerPhoto") val customerPhoto: String? = null,
    val rating: Int,
    val comment: String? = null,
    val reply: String? = null,
    @SerialName("repliedAt") val repliedAt: String? = null,
    @SerialName("createdAt") val createdAt: String
)
