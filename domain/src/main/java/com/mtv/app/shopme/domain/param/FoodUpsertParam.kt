package com.mtv.app.shopme.domain.param

import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import java.math.BigDecimal

data class FoodUpsertParam(
    val cafeId: String,
    val images: List<String>,
    val name: String,
    val description: String,
    val category: FoodCategory,
    val estimate: String,
    val isActive: Boolean,
    val price: BigDecimal,
    val quantity: Long,
    val status: FoodStatus,
    val variants: List<FoodVariantParam>
)

data class FoodVariantParam(
    val name: String,
    val options: List<FoodOptionParam>
)

data class FoodOptionParam(
    val name: String,
    val price: BigDecimal
)
