/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatListViewModel.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.32
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.usecase.GetChatListUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerChatListEffect
import com.mtv.app.shopme.feature.seller.contract.SellerChatListEvent
import com.mtv.app.shopme.feature.seller.contract.SellerChatListItem
import com.mtv.app.shopme.feature.seller.contract.SellerChatListUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
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
class SellerChatListViewModel @Inject constructor(
    private val getChatListUseCase: GetChatListUseCase
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
        observeDataFlow(
            flow = getChatListUseCase(asSeller = true),
            onState = { state ->
                _state.update {
                    val chatItems = (state as? LoadState.Success)
                        ?.data
                        ?.chatList
                        ?.map { item ->
                            SellerChatListItem(
                                id = item.id,
                                name = item.name,
                                lastMessage = item.lastMessage,
                                time = item.time,
                                unreadCount = item.unreadCount,
                                avatarBase64 = item.avatarBase64
                            )
                        }
                        .orEmpty()

                    it.copy(
                        isLoading = state is LoadState.Loading,
                        chatList = if (chatItems.isNotEmpty()) chatItems else it.chatList
                    )
                }
            },
            onError = ::showError
        )
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
