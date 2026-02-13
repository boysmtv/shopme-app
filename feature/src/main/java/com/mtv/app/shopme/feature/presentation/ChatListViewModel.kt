/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ListChatViewModel.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 13.34
 */

package com.mtv.app.shopme.feature.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.data.ChatListItem
import com.mtv.app.shopme.feature.contract.ChatListDataListener
import com.mtv.app.shopme.feature.contract.ChatListStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val securePrefs: SecurePrefs
) : BaseViewModel(), UiOwner<ChatListStateListener, ChatListDataListener> {

    override val uiState = MutableStateFlow(
        ChatListStateListener(
            chatList = mockChatList()
        )
    )

    override val uiData = MutableStateFlow(ChatListDataListener())
}

fun mockChatList() = listOf(
    ChatListItem(
        id = "1",
        name = "Cafe Kopi Kita",
        lastMessage = "Pesanan latte sedang diproses ☕",
        time = "12:30",
        unreadCount = 2
    ),
    ChatListItem(
        id = "2",
        name = "Bakery Mantap",
        lastMessage = "Kue brownies baru keluar oven!",
        time = "10:15",
        unreadCount = 0
    ),
    ChatListItem(
        id = "3",
        name = "Warung Sederhana",
        lastMessage = "Ayam geprek siap dikirim.",
        time = "Kemarin",
        unreadCount = 1
    ),
    ChatListItem(
        id = "4",
        name = "Sushi House",
        lastMessage = "Salmon sushi extra fresh hari ini 🍣",
        time = "09:20",
        unreadCount = 12
    ),
    ChatListItem(
        id = "5",
        name = "Burger Boss",
        lastMessage = "Cheese burger pesanan Anda sedang dibuat.",
        time = "08:45",
        unreadCount = 0
    ),
    ChatListItem(
        id = "6",
        name = "Noodle Station",
        lastMessage = "Mie pedas level 3 siap disantap! 🔥",
        time = "Kemarin",
        unreadCount = 4
    ),
    ChatListItem(
        id = "7",
        name = "Pizza Corner",
        lastMessage = "Promo beli 1 gratis 1 masih tersedia! 🍕",
        time = "2 hari lalu",
        unreadCount = 0
    ),
    ChatListItem(
        id = "8",
        name = "Kedai Teh Manis",
        lastMessage = "Thai tea ukuran large siap diambil.",
        time = "11:05",
        unreadCount = 1
    ),
    ChatListItem(
        id = "9",
        name = "Steak Master",
        lastMessage = "Steak medium rare sedang dipanggang 🥩",
        time = "Hari ini",
        unreadCount = 2
    ),
    ChatListItem(
        id = "10",
        name = "Dimsum Queen",
        lastMessage = "Paket dimsum sudah dikemas.",
        time = "07:50",
        unreadCount = 0
    )
)
