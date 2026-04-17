/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderItem.kt
 *
 * Last modified by Dedy Wijaya on 15/04/26 14.03
 */

package com.mtv.app.shopme.domain.model

data class SellerOrderItem(
    val id: String,
    val invoice: String,
    val customer: String,
    val total: String,
    val date: String,
    val time: String,
    val paymentMethod: String,
    val status: String,
    val location: String
)