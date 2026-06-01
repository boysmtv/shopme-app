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
    val id: String? = null,
    val name: String? = null,
    val block: String? = null,
    val number: String? = null,
    val rt: String? = null,
    val rw: String? = null
)