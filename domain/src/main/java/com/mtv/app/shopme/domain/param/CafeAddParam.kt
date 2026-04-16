/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeAddParam.kt
 *
 * Last modified by Dedy Wijaya on 16/04/26 13.41
 */

package com.mtv.app.shopme.domain.param

data class CafeAddParam(
    val name: String,
    val phone: String = "",
    val description: String = "",
    val minimalOrder: String = "",
    val openTime: String = "",
    val closeTime: String = "",
    val image: String = "",
    val isActive: Boolean = true
)