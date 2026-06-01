/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: VillageAddRequest.kt
 *
 * Last modified by Dedy Wijaya on 01/06/26
 */

package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class VillageAddRequest(
    val name: String
)
