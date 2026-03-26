/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: Cart.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 12.23
 */

package com.mtv.app.shopme.domain.model

import java.math.BigDecimal

data class CartItem(
    val id: String,
    val customerId: String,
    val foodId: String,
    val cafeId: String,
    val cafeName: String,
    val name: String,
    val image: String,
    val price: BigDecimal,
    val quantity: Int,
    val notes: String,
    val variants: List<CartVariant>,
)

data class CartVariant(
    val variantId: String,
    val variantName: String,
    val optionId: String,
    val optionName: String,
    val price: BigDecimal
)