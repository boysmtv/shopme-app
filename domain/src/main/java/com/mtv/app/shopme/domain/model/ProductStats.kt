package com.mtv.app.shopme.domain.model

data class ProductStats(
    val totalProducts: Long = 0,
    val activeProducts: Long = 0,
    val inactiveProducts: Long = 0,
    val readyProducts: Long = 0,
    val jastipProducts: Long = 0,
    val preorderProducts: Long = 0,
    val totalStock: Long = 0,
    val totalSold: Long = 0
)
