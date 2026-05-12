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
    val isSending: Boolean = false,
    val messages: List<SellerChatDetailMessage> = emptyList(),
    val currentMessage: String = "",
    val activeChatId: String = "",
    val chatName: String = "",
    val chatAvatarBase64: String? = null
)

sealed class SellerChatDetailEvent {
    object Load : SellerChatDetailEvent()
    object DismissDialog : SellerChatDetailEvent()

    data class ChangeMessage(val value: String) : SellerChatDetailEvent()
    object SendMessage : SellerChatDetailEvent()

    object ClickBack : SellerChatDetailEvent()
}

sealed class SellerChatDetailEffect {
    object NavigateBack : SellerChatDetailEffect()
}

data class SellerChatDetailMessage(
    val message: String,
    val isFromSeller: Boolean
)
