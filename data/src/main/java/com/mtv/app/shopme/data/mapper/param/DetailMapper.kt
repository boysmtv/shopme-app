/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DetailMapper.kt
 *
 * Last modified by Dedy Wijaya on 27/03/26 22.49
 */

package com.mtv.app.shopme.data.mapper.param

import com.mtv.app.shopme.data.remote.request.FoodAddToCartRequest
import com.mtv.app.shopme.data.remote.request.FoodVariantAddToCartRequest
import com.mtv.app.shopme.domain.model.param.CartAddParam

fun CartAddParam.toRequest() = FoodAddToCartRequest(
    foodId = foodId,
    variants = variants.map {
        FoodVariantAddToCartRequest(
            variantId = it.variantId,
            optionId = it.optionId
        )
    },
    quantity = quantity,
    note = note,
)