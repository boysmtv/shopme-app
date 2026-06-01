/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodBulkStatusRequest.kt
 *
 * Last modified by Dedy Wijaya on 01/06/26
 */

package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class FoodBulkStatusRequest(
    val foodIds: List<String>,
    val isActive: Boolean
)
