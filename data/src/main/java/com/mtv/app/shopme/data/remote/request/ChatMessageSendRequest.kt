/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatMessageSendRequest.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 15.01
 */

package com.mtv.app.shopme.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageSendRequest(
    val id: String,
    val message: String
)