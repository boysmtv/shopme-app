/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartQuantityRequest.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 13.06
 */

package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class CartQuantityRequest(
    var cartId: String,
    var quantity: Int,
)