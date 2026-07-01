package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SellerCategoryRequest(
    val name: String,
    @SerialName("isActive") val isActive: Boolean = true
)
