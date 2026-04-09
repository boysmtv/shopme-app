/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerChatDetailViewModel.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.32
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailEffect
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailEvent
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailMessage
import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailUiState
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
class SellerChatDetailViewModel @Inject constructor(

) : BaseEventViewModel<SellerChatDetailEvent, SellerChatDetailEffect>() {

    private val _state = MutableStateFlow(
        SellerChatDetailUiState(
            messages = mockSellerChatMessages()
        )
    )
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerChatDetailEvent) {
        when (event) {
            is SellerChatDetailEvent.Load -> {}
            is SellerChatDetailEvent.DismissDialog -> dismissDialog()

            is SellerChatDetailEvent.ChangeMessage -> {
                _state.update { it.copy(currentMessage = event.value) }
            }

            is SellerChatDetailEvent.SendMessage -> sendMessage()

            is SellerChatDetailEvent.ClickBack ->
                emitEffect(SellerChatDetailEffect.NavigateBack)
        }
    }

    private fun sendMessage() {
        val message = _state.value.currentMessage
        if (message.isBlank()) return

        val newMessage = SellerChatDetailMessage(
            message = message,
            isFromSeller = true
        )

        _state.update {
            it.copy(
                messages = it.messages + newMessage,
                currentMessage = ""
            )
        }

        simulateCustomerReply()
    }

    private fun simulateCustomerReply() {
        viewModelScope.launch {
            delay(1000)

            _state.update {
                it.copy(
                    messages = it.messages + SellerChatDetailMessage(
                        message = "Baik kak, terima kasih 🙏",
                        isFromSeller = false
                    )
                )
            }
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

fun mockSellerChatMessages(): List<SellerChatDetailMessage> = listOf(
    SellerChatDetailMessage("Halo kak! Ada yang bisa kami bantu?", true),
    SellerChatDetailMessage("Saya mau pesan nasi goreng.", false),
    SellerChatDetailMessage("Siap kak, mau level pedasnya bagaimana?", true),
    SellerChatDetailMessage("Sedang saja.", false),
    SellerChatDetailMessage("Oke 😊 Pesanan akan segera dikirim.", true)
)