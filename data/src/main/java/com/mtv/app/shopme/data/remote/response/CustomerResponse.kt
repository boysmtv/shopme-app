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
    val stats: Stats?,
    val menuSummary: MenuSummary?
)

@Serializable
data class Stats(
    val totalOrders: Int,
    val activeOrders: Int,
    val membership: String
)

@Serializable
data class MenuSummary(
    val ordered: Int,
    val cooking: Int,
    val shipping: Int,
    val completed: Int,
    val cancelled: Int
)