/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatListViewModel.kt
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
class SellerChatListViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : BaseViewModel(),
    UiOwner<SellerChatListStateListener, SellerChatListDataListener> {

    override val uiState = MutableStateFlow(SellerChatListStateListener(chatList = mockSellerChatList()))
    override val uiData = MutableStateFlow(SellerChatListDataListener())

}

fun mockSellerChatList(): List<SellerChatListItem> = listOf(
    SellerChatListItem("1", "Dedy Wijaya", "Terima kasih pesanan sudah sampai", "10:32", 2),
    SellerChatListItem("2", "Rina Sari", "Bisa tambah extra cheese?", "09:45", 0),
    SellerChatListItem("3", "Andi Saputra", "Pesanan sudah dibayar", "Kemarin", 1)
)
