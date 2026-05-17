/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatDetailContract.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.20
 */

package com.mtv.app.shopme.feature.seller.contract

data class SellerChatDetailUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isSending: Boolean = false,
    val messages: List<SellerChatDetailMessage> = emptyList(),
    val currentMessage: String = "",
    val activeChatId: String = "",
    val chatName: String = "",
    val chatAvatarUrl: String? = null,
    val isPeerOnline: Boolean = false,
    val peerLastSeenAt: String = ""
)

sealed class SellerChatDetailEvent {
    object Load : SellerChatDetailEvent()
    object DismissDialog : SellerChatDetailEvent()

    data class ChangeMessage(val value: String) : SellerChatDetailEvent()
    object SendMessage : SellerChatDetailEvent()
    data class RetryMessage(val localId: String, val message: String) : SellerChatDetailEvent()

    object ClickBack : SellerChatDetailEvent()
}

sealed class SellerChatDetailEffect {
    object NavigateBack : SellerChatDetailEffect()
}

data class SellerChatDetailMessage(
    val message: String,
    val isFromSeller: Boolean,
    val id: String = "",
    val time: String = "",
    val isPending: Boolean = false,
    val isRead: Boolean = true,
    val isFailed: Boolean = false
)
