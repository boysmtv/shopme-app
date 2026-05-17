/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatListContract.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.19
 */

package com.mtv.app.shopme.feature.seller.contract

data class SellerChatListUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val chatList: List<SellerChatListItem> = emptyList()
)

sealed class SellerChatListEvent {
    object Load : SellerChatListEvent()
    object DismissDialog : SellerChatListEvent()

    data class ClickChat(val item: SellerChatListItem) : SellerChatListEvent()
    object ClickClearAll : SellerChatListEvent()

    object ClickBack : SellerChatListEvent()
}

sealed class SellerChatListEffect {
    object NavigateBack : SellerChatListEffect()
    data class NavigateToChat(val chatId: String) : SellerChatListEffect()
}

data class SellerChatListItem(
    val id: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int,
    val avatarUrl: String? = null
)
