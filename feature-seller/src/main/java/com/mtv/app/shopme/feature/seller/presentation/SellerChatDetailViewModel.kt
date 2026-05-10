/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatDetailViewModel.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.32
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.usecase.ChatMessageMarkAsReadUseCase
import com.mtv.app.shopme.domain.usecase.CreateChatMessageSendUseCase
import com.mtv.app.shopme.domain.usecase.GetChatMessageUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailEffect
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailEvent
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailMessage
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailUiState
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
class SellerChatDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getChatMessageUseCase: GetChatMessageUseCase,
    private val sendChatMessageUseCase: CreateChatMessageSendUseCase,
    private val chatMessageMarkAsReadUseCase: ChatMessageMarkAsReadUseCase,
) : BaseEventViewModel<SellerChatDetailEvent, SellerChatDetailEffect>() {

    private val routeChatId: String = savedStateHandle.get<String>("chatId").orEmpty()
    private var lastReadChatId: String? = null

    private val _state = MutableStateFlow(
        SellerChatDetailUiState(
            activeChatId = routeChatId
        )
    )
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerChatDetailEvent) {
        when (event) {
            is SellerChatDetailEvent.Load -> load()
            is SellerChatDetailEvent.DismissDialog -> dismissDialog()

            is SellerChatDetailEvent.ChangeMessage -> {
                _state.update { it.copy(currentMessage = event.value) }
            }

            is SellerChatDetailEvent.SendMessage -> sendMessage()

            is SellerChatDetailEvent.ClickBack ->
                emitEffect(SellerChatDetailEffect.NavigateBack)
        }
    }

    private fun load() {
        observeChat()
    }

    private fun observeChat() {
        observeDataFlow(
            flow = getChatMessageUseCase(routeChatId.ifBlank { null }, asSeller = true),
            onState = { state ->
                var nextActiveChatId = _state.value.activeChatId
                _state.update {
                    val activeId = (state as? LoadState.Success)
                        ?.data
                        ?.firstOrNull()
                        ?.id
                        .orEmpty()
                    val messages = (state as? LoadState.Success)
                        ?.data
                        ?.map { item ->
                            SellerChatDetailMessage(
                                message = item.lastMessage,
                                isFromSeller = !item.isFromUser
                            )
                        }
                        .orEmpty()

                    it.copy(
                        activeChatId = it.activeChatId.ifBlank { activeId },
                        messages = if (messages.isNotEmpty()) messages else it.messages
                    ).also { nextState ->
                        nextActiveChatId = nextState.activeChatId
                    }
                }
                markAsReadIfNeeded(nextActiveChatId)
            },
            onError = ::showError
        )
    }

    private fun sendMessage() {
        val message = _state.value.currentMessage
        if (message.isBlank()) return
        val chatId = _state.value.activeChatId
        if (chatId.isBlank()) return

        observeDataFlow(
            flow = sendChatMessageUseCase(chatId, message, asSeller = true),
            onSuccess = {
                _state.update { it.copy(currentMessage = "") }
                observeChat()
            },
            onError = ::showError
        )
    }

    private fun readAll(chatId: String) {
        observeDataFlow(
            flow = chatMessageMarkAsReadUseCase(chatId, asSeller = true),
            onError = ::showError
        )
    }

    private fun markAsReadIfNeeded(chatId: String) {
        if (chatId.isBlank() || lastReadChatId == chatId) return
        lastReadChatId = chatId
        readAll(chatId)
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
