/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: UserResponse.kt
 *
 * Last modified by Dedy Wijaya on 16/04/26 11.15
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val photo: String
)