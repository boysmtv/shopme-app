/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatListContract.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.19
 */

package com.mtv.app.shopme.feature.seller.contract

data class SellerChatListStateListener(
    val chatList: List<SellerChatListItem> = emptyList()
)

data class SellerChatListDataListener(
    val unused: String = ""
)

class SellerChatListEventListener(
    val onDeleteChat: () -> Unit
)

class SellerChatListNavigationListener(
    val onBack: () -> Unit = {},
    val navigateToChat: () -> Unit = {}
)

data class SellerChatListItem(
    val id: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int,
    val avatarBase64: String? = null
)

