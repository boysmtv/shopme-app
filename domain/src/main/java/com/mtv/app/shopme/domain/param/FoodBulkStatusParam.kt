package com.mtv.app.shopme.domain.param

data class FoodBulkStatusParam(
    val foodIds: List<String>,
    val isActive: Boolean
)
