/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: StatsDomain.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 02.32
 */

package com.mtv.app.shopme.domain.model

data class Stats(
    val totalOrders: Int,
    val activeOrders: Int,
    val membership: String
)