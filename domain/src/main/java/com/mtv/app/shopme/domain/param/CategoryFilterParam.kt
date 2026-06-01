package com.mtv.app.shopme.domain.param

data class CategoryFilterParam(
    val category: String,
    val page: Int = 0,
    val size: Int = 20
)
