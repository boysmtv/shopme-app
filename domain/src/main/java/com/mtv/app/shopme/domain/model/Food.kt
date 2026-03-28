/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: Food.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 00.54
 */

package com.mtv.app.shopme.domain.model

import java.math.BigDecimal
import org.threeten.bp.LocalDateTime

data class Food(
    val id: String,
    val cafeId: String,
    val name: String,
    val cafeName: String,
    val cafeAddress: String,
    val description: String,
    val price: BigDecimal,
    val category: FoodCategory,
    val status: FoodStatus,
    val quantity: Long,
    val estimate: String,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val images: List<String>,
    val variants: List<FoodVariant>
)