/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartMapper.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 15.39
 */

package com.mtv.app.shopme.data.mapper.param

import com.mtv.app.shopme.data.remote.request.CartQuantityRequest
import com.mtv.app.shopme.data.remote.request.CreateOrderRequest
import com.mtv.app.shopme.data.remote.request.VerifyPinRequest
import com.mtv.app.shopme.domain.model.param.CartQuantityParam
import com.mtv.app.shopme.domain.model.param.CreateOrderParam
import com.mtv.app.shopme.domain.model.param.VerifyPinParam

fun VerifyPinParam.toRequest() = VerifyPinRequest(
    token = token,
    pin = pin
)

fun CreateOrderParam.toRequest() = CreateOrderRequest(
    cartId = cartId,
    payment = payment,
    token = token,
)

fun CartQuantityParam.toRequest() = CartQuantityRequest(
    quantity = quantity,
)