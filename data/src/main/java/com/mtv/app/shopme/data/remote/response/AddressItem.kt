/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AddressItem.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 15.50
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class AddressItem(
    val id: String,
    val areaId: String,
    val block: String,
    val number: String,
    val rt: String,
    val rw: String,
    val isDefault: Boolean
)