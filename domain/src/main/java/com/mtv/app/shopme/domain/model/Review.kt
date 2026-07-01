package com.mtv.app.shopme.domain.model

data class Review(
    val id: String,
    val foodId: String,
    val foodName: String?,
    val cafeId: String,
    val customerName: String?,
    val customerPhoto: String?,
    val rating: Int,
    val comment: String?,
    val reply: String?,
    val repliedAt: String?,
    val createdAt: String
)
