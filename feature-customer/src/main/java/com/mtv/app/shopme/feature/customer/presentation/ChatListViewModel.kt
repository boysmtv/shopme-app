/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ListChatViewModel.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 13.34
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.data.local.NotificationItem
import com.mtv.app.shopme.domain.model.ChatListItem
import com.mtv.app.shopme.domain.usecase.ChatListUseCase
import com.mtv.app.shopme.feature.customer.contract.ChatListEffect
import com.mtv.app.shopme.feature.customer.contract.ChatListEffect.*
import com.mtv.app.shopme.feature.customer.contract.ChatListEvent
import com.mtv.app.shopme.feature.customer.contract.ChatListUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatListUseCase: ChatListUseCase
) : BaseEventViewModel<ChatListEvent, ChatListEffect>() {

    private val _state = MutableStateFlow(ChatListUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: ChatListEvent) {
        when (event) {
            is ChatListEvent.Load -> observeChatList()
            is ChatListEvent.DismissDialog -> dismissDialog()
            is ChatListEvent.ClickItem -> emitEffect(NavigateToChat(event.id))
            is ChatListEvent.ClickBack -> TODO()
        }
    }

    private fun observeChatList() {
        observeDataFlow(
            flow = chatListUseCase(),
            onLoad = ::showLoading,
            onState = { state ->
                _state.update {
                    it.copy(chatListState = state)
                }
            },
            onError = ::showError
        )
    }

    private fun showError(error: UiError) {
        setDialog(
            UiDialog.Center(
                state = DialogStateV1(
                    type = DialogType.ERROR,
                    title = ErrorMessages.GENERIC_ERROR,
                    message = error.message
                ),
                onPrimary = { dismissDialog() }
            )
        )
    }
}

fun mockChatList() = listOf(
    ChatListItem(
        id = "1",
        name = "Cafe Kopi Kita",
        lastMessage = "Pesanan latte sedang diproses ☕",
        time = "12:30",
        unreadCount = 2,
        avatarBase64 = ""
    ),
    ChatListItem(
        id = "2",
        name = "Bakery Mantap",
        lastMessage = "Kue brownies baru keluar oven!",
        time = "10:15",
        unreadCount = 0,
        avatarBase64 = ""
    ),
    ChatListItem(
        id = "3",
        name = "Warung Sederhana",
        lastMessage = "Ayam geprek siap dikirim.",
        time = "Kemarin",
        unreadCount = 1,
        avatarBase64 = ""
    ),
    ChatListItem(
        id = "4",
        name = "Sushi House",
        lastMessage = "Salmon sushi extra fresh hari ini 🍣",
        time = "09:20",
        unreadCount = 12,
        avatarBase64 = ""
    ),
    ChatListItem(
        id = "5",
        name = "Burger Boss",
        lastMessage = "Cheese burger pesanan Anda sedang dibuat.",
        time = "08:45",
        unreadCount = 0,
        avatarBase64 = ""
    ),
    ChatListItem(
        id = "6",
        name = "Noodle Station",
        lastMessage = "Mie pedas level 3 siap disantap! 🔥",
        time = "Kemarin",
        unreadCount = 4,
        avatarBase64 = ""
    ),
    ChatListItem(
        id = "7",
        name = "Pizza Corner",
        lastMessage = "Promo beli 1 gratis 1 masih tersedia! 🍕",
        time = "2 hari lalu",
        unreadCount = 0,
        avatarBase64 = ""
    ),
    ChatListItem(
        id = "8",
        name = "Kedai Teh Manis",
        lastMessage = "Thai tea ukuran large siap diambil.",
        time = "11:05",
        unreadCount = 1,
        avatarBase64 = ""
    ),
    ChatListItem(
        id = "9",
        name = "Steak Master",
        lastMessage = "Steak medium rare sedang dipanggang 🥩",
        time = "Hari ini",
        unreadCount = 2,
        avatarBase64 = ""
    ),
    ChatListItem(
        id = "10",
        name = "Dimsum Queen",
        lastMessage = "Paket dimsum sudah dikemas.",
        time = "07:50",
        unreadCount = 0,
        avatarBase64 = ""
    )
)

val previewNotification = NotificationItem(
    title = "Watch Fast & Furious 7",
    message = "Ini masuk ke semua device yang subscribe topic",
    photo = "https://i.pinimg.com/236x/94/25/fa/9425faaad20d8f527e178a34435734be.jpg",
    signatureName = "Dsrv Developer",
    signatureDate = "Feb 11, 2026",
    signatureTime = "10.00",
    isRead = false
)

