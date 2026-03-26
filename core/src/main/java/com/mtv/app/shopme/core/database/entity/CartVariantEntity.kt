/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartVariantEntity.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 12.33
 */

package com.mtv.app.shopme.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_variant")
data class CartVariantEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cartId: String,
    val optionName: String,
    val price: String
)