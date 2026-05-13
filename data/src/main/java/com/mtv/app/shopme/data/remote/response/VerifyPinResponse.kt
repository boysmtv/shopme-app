/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: VerifyPinResponse.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 13.15
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class VerifyPinResponse(
    val verified: Boolean,
)
