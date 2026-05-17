/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatList.kt
 *
 * Last modified by Dedy Wijaya on 30/03/26 16.41
 */

package com.mtv.app.shopme.domain.model

data class ChatListItem(
    val id: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int,
    val avatarUrl: String?,
    val isFromUser: Boolean = false,
    val isPending: Boolean = false,
    val isRead: Boolean = true,
    val isFailed: Boolean = false,
)
