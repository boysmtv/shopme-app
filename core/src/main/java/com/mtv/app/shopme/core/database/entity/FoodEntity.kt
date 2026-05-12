/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodEntity.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 19.36
 */

package com.mtv.app.shopme.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food")
data class FoodEntity(
    @PrimaryKey val id: String,
    val name: String,
    val price: Double,
    val image: String,
    val cafeName: String,
    val isActive: Boolean
)
