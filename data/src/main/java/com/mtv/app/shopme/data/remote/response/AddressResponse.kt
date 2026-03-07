/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AddressResponse.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 15.49
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class AddressResponse(
    var addressList: List<AddressItem>,
)