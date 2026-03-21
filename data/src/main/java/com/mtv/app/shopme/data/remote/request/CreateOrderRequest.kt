/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: VerifyPinRequest.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 13.14
 */

package com.mtv.app.shopme.data.remote.request

import com.mtv.app.shopme.data.dto.PaymentMethod
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequest(
    val cartId: List<String>,
    val payment: PaymentMethod,
    val token: String
)