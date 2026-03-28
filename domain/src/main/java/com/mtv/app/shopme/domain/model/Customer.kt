/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: Customer.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 00.55
 */

package com.mtv.app.shopme.domain.model

data class Customer(
    val name: String,
    val phone: String,
    val email: String,
    val address: Address?,
    val photo: String,
    val verified: Boolean,
    val stats: Stats?,
    val menuSummary: MenuSummary?,
)

val EMPTY = Customer(
    name = "",
    phone = "",
    email = "",
    address = null,
    photo = "",
    verified = false,
    stats = null,
    menuSummary = null,
)