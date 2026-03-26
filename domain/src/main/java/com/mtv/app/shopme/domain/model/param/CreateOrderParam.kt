/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CreateOrderParam.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 15.34
 */

package com.mtv.app.shopme.domain.model.param

import com.mtv.app.shopme.domain.model.PaymentMethod

data class CreateOrderParam(
    val cartId: List<String>,
    val payment: PaymentMethod,
    val token: String
)