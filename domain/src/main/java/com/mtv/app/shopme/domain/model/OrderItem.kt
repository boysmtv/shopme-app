/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderItem.kt
 *
 * Last modified by Dedy Wijaya on 15/04/26 14.45
 */

package com.mtv.app.shopme.domain.model

import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class OrderItem(
    val id: String = EMPTY_STRING,
    val foodId: String = EMPTY_STRING,
    val quantity: Int = 0,
    val price: Double = 0.0,
    val notes: String? = null,
    val status: OrderItemStatus = OrderItemStatus.AVAILABLE
)