package com.mtv.app.shopme.data.remote.request

import com.mtv.app.shopme.common.serializer.BigDecimalSerializer
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import java.math.BigDecimal
import kotlinx.serialization.Serializable

@Serializable
data class FoodUpsertRequest(
    val cafeId: String,
    val image: List<FoodImageRequest>? = null,
    val name: String,
    val description: String,
    val category: FoodCategory,
    val estimate: String,
    val isActive: Boolean,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal,
    val quantity: Long,
    val status: FoodStatus,
    val variant: List<FoodVariantRequest>? = null
)

@Serializable
data class FoodImageRequest(
    val image: String
)

@Serializable
data class FoodVariantRequest(
    val name: String,
    val option: List<FoodOptionRequest>? = null
)

@Serializable
data class FoodOptionRequest(
    val name: String,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal
)
