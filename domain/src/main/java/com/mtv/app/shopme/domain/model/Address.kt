/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: Address.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 02.35
 */

package com.mtv.app.shopme.domain.model

data class Address(
    val id: String,
    val village: String,
    val block: String,
    val number: String,
    val rt: String,
    val rw: String,
    val isDefault: Boolean
)