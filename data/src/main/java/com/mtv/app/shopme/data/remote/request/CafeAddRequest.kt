/*
 * Project: Boys.mtv@gmail.com
 * File: CafeAddRequest.kt
 *
 * Last modified by Dedy Wijaya on 13/03/2026 13.45
 */

package com.mtv.app.shopme.data.remote.request


data class CafeAddRequest(
    val name: String,
    val phone: String = "",
    val description: String = "",
    val minimalOrder: String = "",
    val openTime: String = "",
    val closeTime: String = "",
    val image: String = "",
    val isActive: Boolean = true
)