/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeAddress.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 22.19
 */

package com.mtv.app.shopme.domain.model

data class CafeAddress(
    val id: String,
    val name: String,
    val block: String,
    val number: String,
    val rt: String,
    val rw: String
)