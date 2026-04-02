/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.domain.model.ChatListItem
import com.mtv.based.core.network.utils.LoadState

data class ChatUiState(
    val chats: LoadState<List<ChatListItem>> = LoadState.Idle,
    val sendMessage: LoadState<Unit> = LoadState.Idle,
    val readAll: LoadState<Unit> = LoadState.Idle
)

sealed class ChatEvent {
    object Load : ChatEvent()
    object DismissDialog : ChatEvent()

    data class SendMessage(val id: String, val message: String) : ChatEvent()
    data class ReadAllMessage(val id: String) : ChatEvent()

    object ClickBack : ChatEvent()
}

sealed class ChatEffect {
    object NavigateBack : ChatEffect()
}