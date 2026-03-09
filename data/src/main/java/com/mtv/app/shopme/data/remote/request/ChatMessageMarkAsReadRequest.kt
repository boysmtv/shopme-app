/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatMessageMarkAsReadRequest.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 15.03
 */

package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageMarkAsReadRequest(
    val id: String,
    val message: String
)