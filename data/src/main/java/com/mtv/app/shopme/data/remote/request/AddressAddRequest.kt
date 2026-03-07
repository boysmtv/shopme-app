/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartInquiryRequest.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 13.24
 */

package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class AddressAddRequest(
    val areaId: String,
    val block: String,
    val number: String,
    val rt: String,
    val rw: String,
    val isDefault: Boolean
)