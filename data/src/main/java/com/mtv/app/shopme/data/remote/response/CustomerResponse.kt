/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CustomerResponse.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 12.16
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class CustomerResponse(
    val name: String?,
    val phone: String?,
    val email: String?,
    val address: AddressResponse?,
    val photo: String?,
    val verified: Boolean?,
    val stats: StatsResponse?,
    val menuSummary: MenuSummaryResponse?,
)

@Serializable
data class StatsResponse(
    val totalOrders: Int,
    val activeOrders: Int,
    val membership: String
)

@Serializable
data class MenuSummaryResponse(
    val ordered: Int,
    val cooking: Int,
    val shipping: Int,
    val completed: Int,
    val cancelled: Int
)