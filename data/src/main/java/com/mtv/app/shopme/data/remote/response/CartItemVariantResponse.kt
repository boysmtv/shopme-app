/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartItemVariantResponse.kt
 *
 * Last modified by Dedy Wijaya on 19/03/26 02.18
 */

package com.mtv.app.shopme.data.remote.response

import com.mtv.app.shopme.common.serializer.BigDecimalSerializer
import java.math.BigDecimal
import kotlinx.serialization.Serializable

@Serializable
data class CartItemVariantResponse(
    val variantId: String,
    val variantName: String,
    val optionId: String,
    val optionName: String,

    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal
)