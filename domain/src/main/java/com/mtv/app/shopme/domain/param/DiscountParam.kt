package com.mtv.app.shopme.domain.param

import com.mtv.app.shopme.domain.model.DiscountType

data class DiscountParam(
    val name: String,
    val type: DiscountType,
    val value: String,
    val minOrder: String? = null,
    val maxDiscount: String? = null,
    val startDate: String,
    val endDate: String,
    val isActive: Boolean = true
)
