/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodOption.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 02.33
 */

package com.mtv.app.shopme.domain.model

import java.math.BigDecimal

data class FoodOption(
    val id: String,
    val name: String,
    val price: BigDecimal
)