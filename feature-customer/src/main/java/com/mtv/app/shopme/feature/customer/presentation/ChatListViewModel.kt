/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ListChatViewModel.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 13.34
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.usecase.GetChatListUseCase
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
    private val chatListUseCase: GetChatListUseCase
) : BaseEventViewModel<ChatListEvent, ChatListEffect>() {

    private val _state = MutableStateFlow(ChatListUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: ChatListEvent) {
        when (event) {
            is ChatListEvent.Load -> observeChatList()
            is ChatListEvent.DismissDialog -> dismissDialog()
            is ChatListEvent.ClickItem -> emitEffect(NavigateToChat(event.id))
            is ChatListEvent.ClickBack -> emitEffect(NavigateBack)
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
