/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodItemModel.kt
 *
 * Last modified by Dedy Wijaya on 18/03/26 02.15
 */

package com.mtv.app.shopme.data.dto

import java.math.BigDecimal

data class FoodItemModel(
    val id: String,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val imageUrl: String,
    val isActive: Boolean,
    val cafeName: String,
    val cafeAddress: String
)