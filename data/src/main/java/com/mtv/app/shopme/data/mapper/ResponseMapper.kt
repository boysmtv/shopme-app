/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ResponseMapper.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 23.56
 */

package com.mtv.app.shopme.data.mapper

import com.mtv.app.shopme.data.remote.response.FoodOptionResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.remote.response.FoodVariantResponse
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.FoodOption
import com.mtv.app.shopme.domain.model.FoodVariant

/* =========================================================
 * DOMAIN → RESPONSE (PRIVATE)
 * ========================================================= */

private fun Food.toResponse(): FoodResponse = FoodResponse(
    id = id,
    cafeId = cafeId,
    name = name,
    cafeName = cafeName,
    cafeAddress = cafeAddress,
    description = description,
    price = price,
    category = category,
    status = status,
    quantity = quantity,
    estimate = estimate,
    isActive = isActive,
    createdAt = createdAt,
    images = images,
    variants = variants.map { it.toResponse() }
)

private fun FoodVariant.toResponse(): FoodVariantResponse =
    FoodVariantResponse(
        id = id,
        name = name,
        options = options.map { it.toResponse() }
    )

private fun FoodOption.toResponse(): FoodOptionResponse =
    FoodOptionResponse(
        id = id,
        name = name,
        price = price
    )