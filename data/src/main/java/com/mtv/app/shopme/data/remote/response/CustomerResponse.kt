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
    val totalOrders: Long,
    val activeOrders: Long,
    val membership: String
)

@Serializable
data class MenuSummaryResponse(
    val unpaid: Long = 0,
    val ordered: Long,
    val cooking: Long,
    val shipping: Long,
    val completed: Long,
    val cancelled: Long
)
