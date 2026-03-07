/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartItemResponse.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 12.58
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class CartItemResponse(
    val id: String,
    val customerId: String,
    val foodId: String,
    val quantity: Int,
    val notes: String,
    val createdAt: String
)