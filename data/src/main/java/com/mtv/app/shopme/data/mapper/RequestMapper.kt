/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: RequestMapper.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 23.55
 */

package com.mtv.app.shopme.data.mapper

import com.mtv.app.shopme.data.remote.request.AddressAddRequest
import com.mtv.app.shopme.data.remote.request.CartQuantityRequest
import com.mtv.app.shopme.data.remote.request.CreateOrderRequest
import com.mtv.app.shopme.data.remote.request.CustomerUpdateRequest
import com.mtv.app.shopme.data.remote.request.FoodAddToCartRequest
import com.mtv.app.shopme.data.remote.request.FoodVariantAddToCartRequest
import com.mtv.app.shopme.data.remote.request.VerifyPinRequest
import com.mtv.app.shopme.domain.param.AddressAddParam
import com.mtv.app.shopme.domain.param.CartAddParam
import com.mtv.app.shopme.domain.param.CartQuantityParam
import com.mtv.app.shopme.domain.param.CreateOrderParam
import com.mtv.app.shopme.domain.param.CustomerUpdateParam
import com.mtv.app.shopme.domain.param.VerifyPinParam

/* =========================================================
 * PARAM → REQUEST
 * ========================================================= */

fun VerifyPinParam.toRequest() = VerifyPinRequest(
    token = token,
    pin = pin
)

fun CreateOrderParam.toRequest() = CreateOrderRequest(
    cartId = cartId,
    payment = payment,
    token = token
)

fun CartQuantityParam.toRequest() = CartQuantityRequest(
    quantity = quantity
)

fun CartAddParam.toRequest() = FoodAddToCartRequest(
    foodId = foodId,
    variants = variants.map {
        FoodVariantAddToCartRequest(
            variantId = it.variantId,
            optionId = it.optionId
        )
    },
    quantity = quantity,
    note = note
)

fun CustomerUpdateParam.toRequest() = CustomerUpdateRequest(
    name = name,
    phone = phone,
    photo = photo
)

fun AddressAddParam.toRequest() = AddressAddRequest(
    villageId = villageId,
    block = block,
    number = number,
    rt = rt,
    rw = rw,
    isDefault = isDefault
)