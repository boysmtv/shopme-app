/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodAddToCartRequest.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 22.30
 */

package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class FoodAddToCartRequest(
    val foodId: String,
    val variants: List<CartVariantRequest>,
    val quantity: Int,
    val note: String
)

@Serializable
data class CartVariantRequest(
    val variantId: String,
    val optionId: String
)