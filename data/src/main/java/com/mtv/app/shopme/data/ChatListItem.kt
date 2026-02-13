/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatListItem.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 13.33
 */

package com.mtv.app.shopme.data

data class ChatListItem(
    val id: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val avatarBase64: String? = null
)
