/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AddressResponse.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 15.50
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class VillageResponse(
    val id: String,
    val name: String
)