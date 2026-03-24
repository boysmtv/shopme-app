/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodVariant.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 02.32
 */

package com.mtv.app.shopme.domain.model

data class FoodVariant(
    val id: String,
    val name: String,
    val options: List<FoodOption>
)