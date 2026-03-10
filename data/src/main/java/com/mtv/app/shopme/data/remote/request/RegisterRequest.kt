/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: RegisterRequest.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 01.01
 */

package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    var email: String,
    var name: String,
    var password: String?
)