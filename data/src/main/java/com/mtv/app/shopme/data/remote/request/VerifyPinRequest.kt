/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: VerifyPinRequest.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 13.14
 */

package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class VerifyPinRequest(
    var trxId: String,
    var pin: String?
)
