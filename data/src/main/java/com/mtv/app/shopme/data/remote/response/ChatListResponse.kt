/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatListResponse.kt
 *
 * Last modified by Dedy Wijaya on 07/03/26 14.26
 */

package com.mtv.app.shopme.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatListResponse(
    var chatList: List<ChatListItemResponse>,
)