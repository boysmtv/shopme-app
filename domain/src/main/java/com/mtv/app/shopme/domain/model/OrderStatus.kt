/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderStatus.kt
 *
 * Last modified by Dedy Wijaya on 15/04/26 14.44
 */

package com.mtv.app.shopme.domain.model

enum class OrderStatus(val value: String) {
    ORDERED("ORDERED"),
    COOKING("COOKING"),
    DELIVERING("DELIVERING"),
    COMPLETED("COMPLETED");

    companion object {
        fun fromValue(value: String): OrderStatus {
            return OrderStatus.entries.find { it.value == value.uppercase() } ?: ORDERED
        }
    }
}