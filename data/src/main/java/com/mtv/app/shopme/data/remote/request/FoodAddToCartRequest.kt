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
data class FoodAddToCartRequest (
    val foodId: String,
    val sizeId: String,
    val variantId: String,
    val additionalId: String,
)