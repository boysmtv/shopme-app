package com.mtv.app.shopme.domain.model

data class SellerDiscount(
    val id: String,
    val cafeId: String,
    val name: String,
    val type: DiscountType,
    val value: String,
    val minOrder: String?,
    val maxDiscount: String?,
    val startDate: String,
    val endDate: String,
    val isActive: Boolean,
    val createdAt: String
)

enum class DiscountType {
    PERCENTAGE,
    FIXED
}
