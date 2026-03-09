/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodResponse.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 13.48
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class FoodResponse(
    val id: String,
    val cafeId: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val status: String,
    val quantity: Int,
    val estimate: String,
    val isActive: Boolean,
    val createdAt: String
)