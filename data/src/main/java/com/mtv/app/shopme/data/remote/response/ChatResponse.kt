/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatResponse.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 14.55
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val chatList: List<ChatItem>
)

@Serializable
data class ChatItem(
    val id: String,
    val message: String,
    val isFromUser: Boolean
)