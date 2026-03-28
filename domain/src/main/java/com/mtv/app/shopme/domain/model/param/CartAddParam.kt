/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartAddParam.kt
 *
 * Last modified by Dedy Wijaya on 27/03/26 22.42
 */

package com.mtv.app.shopme.domain.model.param

data class CartAddParam(
    val foodId: String,
    val variants: List<CartAddVariantParam>,
    val quantity: Int,
    val note: String
)