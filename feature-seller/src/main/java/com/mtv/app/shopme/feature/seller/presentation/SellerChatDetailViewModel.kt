/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatDetailViewModel.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.32
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.seller.contract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SellerChatDetailViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : BaseViewModel(),
    UiOwner<SellerChatDetailStateListener, SellerChatDetailDataListener> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(SellerChatDetailStateListener(messages = mockSellerChatMessages()))
    override val uiData = MutableStateFlow(SellerChatDetailDataListener())

    fun sendMessage(message: String) {
        val newMessage = SellerChatDetailMessage(message, isFromSeller = true)
        val updatedMessages = uiState.value.messages + newMessage
        uiState.value = uiState.value.copy(messages = updatedMessages)
    }
}

fun mockSellerChatMessages(): List<SellerChatDetailMessage> = listOf(
    SellerChatDetailMessage("Halo kak! Ada yang bisa kami bantu?", true),
    SellerChatDetailMessage("Saya mau pesan nasi goreng.", false),
    SellerChatDetailMessage("Siap kak, mau level pedasnya bagaimana?", true),
    SellerChatDetailMessage("Sedang saja.", false),
    SellerChatDetailMessage("Oke 😊 Pesanan akan segera dikirim.", true)
)
