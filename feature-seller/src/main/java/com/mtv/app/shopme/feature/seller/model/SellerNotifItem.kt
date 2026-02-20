/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerNotifItem.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.27
 */

package com.mtv.app.shopme.feature.seller.model

data class SellerNotifItem(
    val title: String,
    val message: String,
    val orderId: String,
    val buyerName: String,
    val date: String,
    val time: String,
    val isRead: Boolean,
)