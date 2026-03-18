/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchMapper.kt
 *
 * Last modified by Dedy Wijaya on 18/03/26 00.44
 */

package com.mtv.app.shopme.domain.mapper

import com.mtv.app.shopme.data.dto.FoodItemModel
import com.mtv.app.shopme.data.dto.OwnerCafeModel
import com.mtv.app.shopme.data.remote.response.CafeResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse

fun FoodResponse.toUiModel(): FoodItemModel {
    return FoodItemModel(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = images.firstOrNull().orEmpty(),
        isActive = isActive,
        cafeName = cafeName,
        cafeAddress = cafeAddress
    )
}

fun CafeResponse.toUiModel(): OwnerCafeModel {
    return OwnerCafeModel(
        id = id,
        name = name,
        phone = phone,
        openTime = openTime,
        closeTime = closeTime,
        minimalOrder = minimalOrder,
        description = description,
        imageUrl = image,
        isActive = isActive,
        address = "${address.name} - Blok ${address.block}/${address.number}"
    )
}