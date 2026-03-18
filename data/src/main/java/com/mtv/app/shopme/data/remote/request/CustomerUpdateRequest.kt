/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CustomerUpdateRequest.kt
 *
 * Last modified by Dedy Wijaya on 15/03/26 01.04
 */

package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class CustomerUpdateRequest(
    val name: String,
    val phone: String,
    val photo: String
)
