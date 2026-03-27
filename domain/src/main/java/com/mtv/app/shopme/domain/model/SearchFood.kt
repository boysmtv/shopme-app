/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchFood.kt
 *
 * Last modified by Dedy Wijaya on 26/03/26 13.48
 */

package com.mtv.app.shopme.domain.model

import java.math.BigDecimal

data class SearchFood(
    val id: String,
    val name: String,
    val price: BigDecimal,
    val image: String,
    val cafeName: String
)