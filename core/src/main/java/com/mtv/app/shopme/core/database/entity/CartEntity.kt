package com.mtv.app.shopme.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey val id: String,
    val foodId: String,
    val cafeId: String,
    val cafeName: String,
    val name: String,
    val image: String,
    val price: String,
    val quantity: Int,
    val notes: String,
    val updatedAt: Long
)