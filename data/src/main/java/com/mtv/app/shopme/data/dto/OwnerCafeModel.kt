/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OwnerCafeModel.kt
 *
 * Last modified by Dedy Wijaya on 18/03/26 02.15
 */

package com.mtv.app.shopme.data.dto

import java.math.BigDecimal

data class OwnerCafeModel(
    val id: String,
    val name: String,
    val address: String,
    val phone: String,
    val openTime: String,
    val closeTime: String,
    val minimalOrder: BigDecimal,
    val description: String,
    val imageUrl: String,
    val isActive: Boolean
)