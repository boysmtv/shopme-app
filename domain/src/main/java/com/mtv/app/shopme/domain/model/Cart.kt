/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: Cart.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 12.23
 */

package com.mtv.app.shopme.domain.model

import java.math.BigDecimal

data class Cart(
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