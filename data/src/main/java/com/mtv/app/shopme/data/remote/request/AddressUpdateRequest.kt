/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AddressUpdateRequest.kt
 *
 * Last modified by Dedy Wijaya on 01/06/26
 */

package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class AddressUpdateRequest(
    val villageId: String,
    val block: String,
    val number: String,
    val rt: String,
    val rw: String,
    val isDefault: Boolean = false
)
