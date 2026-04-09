/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatListViewModel.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.32
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.seller.contract.SellerChatListEffect
import com.mtv.app.shopme.feature.seller.contract.SellerChatListEvent
import com.mtv.app.shopme.feature.seller.contract.SellerChatListItem
import com.mtv.app.shopme.feature.seller.contract.SellerChatListUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SellerChatListViewModel @Inject constructor(

) : BaseEventViewModel<SellerChatListEvent, SellerChatListEffect>() {

    private val _state = MutableStateFlow(SellerChatListUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerChatListEvent) {
        when (event) {
            is SellerChatListEvent.Load -> loadChats()
            is SellerChatListEvent.DismissDialog -> dismissDialog()

            is SellerChatListEvent.ClickChat ->
                emitEffect(SellerChatListEffect.NavigateToChat(event.item.id))

            is SellerChatListEvent.DeleteChat ->
                deleteChat(event.item)

            is SellerChatListEvent.ClickBack ->
                emitEffect(SellerChatListEffect.NavigateBack)
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            delay(500)

            _state.update {
                it.copy(
                    isLoading = false,
                    chatList = mockSellerChatList()
                )
            }
        }
    }

    private fun deleteChat(item: SellerChatListItem) {
        _state.update {
            it.copy(
                chatList = it.chatList.filter { chat -> chat.id != item.id }
            )
        }
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

fun mockSellerChatList(): List<SellerChatListItem> = listOf(
    SellerChatListItem("1", "Dedy Wijaya", "Terima kasih pesanan sudah sampai", "10:32", 2),
    SellerChatListItem("2", "Rina Sari", "Bisa tambah extra cheese?", "09:45", 0),
    SellerChatListItem("3", "Andi Saputra", "Pesanan sudah dibayar", "Kemarin", 1)
)