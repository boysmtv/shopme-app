/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: Cafe.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 22.19
 */

package com.mtv.app.shopme.domain.model

import java.math.BigDecimal

data class Cafe(
    val id: String,
    val name: String,
    val phone: String,
    val description: String,
    val minimalOrder: BigDecimal,
    val openTime: String,
    val closeTime: String,
    val image: String,
    val isActive: Boolean,
    val address: CafeAddress
)
