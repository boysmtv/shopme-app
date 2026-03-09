/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartItemResponse.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 12.58
 */

package com.mtv.app.shopme.data.remote.response

import com.mtv.app.shopme.common.serializer.BigDecimalSerializer
import java.math.BigDecimal
import kotlinx.serialization.Serializable

@Serializable
data class CartItemResponse(
    val id: String,
    val name: String,
    val image: String,
    val quantity: Int,
    val notes: String,
    val cafeId: String,
    val cafeName: String,
    val foodId: String,
    val customerId: String,

    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal,
)