/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartVariant.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 23.24
 */

package com.mtv.app.shopme.domain.model

import java.math.BigDecimal


data class CartVariant(
    val variantId: String,
    val variantName: String,
    val optionId: String,
    val optionName: String,
    val price: BigDecimal
)