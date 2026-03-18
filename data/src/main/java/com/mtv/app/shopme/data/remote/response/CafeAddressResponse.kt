/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeAddressResponse.kt
 *
 * Last modified by Dedy Wijaya on 18/03/26 02.08
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class CafeAddressResponse(
    val id: String,
    val name: String,
    val block: String,
    val number: String,
    val rt: String,
    val rw: String
)