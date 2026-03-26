/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartMapper.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 12.34
 */

package com.mtv.app.shopme.data.mapper

import com.mtv.app.shopme.data.remote.response.CartItemResponse
import com.mtv.app.shopme.domain.model.CartItem
import com.mtv.app.shopme.domain.model.CartVariant

// 🔥 RESPONSE → DOMAIN
fun CartItemResponse.toDomain(): CartItem =
    CartItem(
        id = id,
        customerId = customerId,
        foodId = foodId,
        cafeId = cafeId,
        cafeName = cafeName,
        name = name,
        image = image,
        price = price,
        quantity = quantity,
        notes = notes,
        variants = variants.map {
            CartVariant(
                variantId = it.variantId,
                variantName = it.variantName,
                optionId = it.optionId,
                optionName = it.optionName,
                price = it.price
            )
        },
    )