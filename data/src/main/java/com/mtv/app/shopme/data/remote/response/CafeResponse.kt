/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeResponse.kt
 *
 * Last modified by Dedy Wijaya on 18/03/26 02.06
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class CafeResponse(
    val id: String,
    val name: String,
    val phone: String,
    val description: String,
    val minimalOrder: String,
    val openTime: String,
    val closeTime: String,
    val image: String,
    val isActive: Boolean,
    val address: CafeAddressResponse? = null
)
