/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatListContract.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 13.33
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.data.ChatListItem

data class ChatListStateListener(
    val chatList: List<ChatListItem> = emptyList()
)

data class ChatListDataListener(
    val emptyData: String? = null
)

data class ChatListEventListener(
    val onClickItem: (String) -> Unit = {}
)

data class ChatListNavigationListener(
    val onBack: () -> Unit = {},
    val navigateToChat: (String) -> Unit = {}
)
