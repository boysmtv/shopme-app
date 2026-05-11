/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatListContract.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 13.33
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.domain.model.ChatList
import com.mtv.based.core.network.utils.LoadState

data class ChatListUiState(
    val chatListState: LoadState<ChatList> = LoadState.Idle,
)

sealed class ChatListEvent {
    object Load : ChatListEvent()
    object DismissDialog : ChatListEvent()
    object ClickBack : ChatListEvent()
    object ClickClearAll : ChatListEvent()
    data class ClickItem(val id: String) : ChatListEvent()
}

sealed class ChatListEffect {
    object NavigateBack : ChatListEffect()
    data class NavigateToChat(val id: String) : ChatListEffect()
}
