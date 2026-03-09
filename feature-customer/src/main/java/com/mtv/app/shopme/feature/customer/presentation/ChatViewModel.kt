/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.45
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.common.valueFlowOf
import com.mtv.app.shopme.data.remote.request.CartQuantityRequest
import com.mtv.app.shopme.data.remote.request.ChatMessageMarkAsReadRequest
import com.mtv.app.shopme.data.remote.request.ChatMessageSendRequest
import com.mtv.app.shopme.domain.usecase.ChatMessageMarkAsReadUseCase
import com.mtv.app.shopme.domain.usecase.ChatMessageSendUseCase
import com.mtv.app.shopme.domain.usecase.ChatMessageUseCase
import com.mtv.app.shopme.feature.customer.contract.ChatDataListener
import com.mtv.app.shopme.feature.customer.contract.ChatStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.update

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatMessageUseCase: ChatMessageUseCase,
    private val chatSendMessageUseCase: ChatMessageSendUseCase,
    private val chatMessageMarkAsReadUseCase: ChatMessageMarkAsReadUseCase,
) : BaseViewModel(), UiOwner<ChatStateListener, ChatDataListener> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(ChatStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(ChatDataListener())

    init {
        //doFetchChat()
    }

    fun doFetchChat() {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.chatState },
                set = { state -> copy(chatState = state) }
            ),
            block = {
                chatMessageUseCase(Unit)
            }
        )
    }

    fun doSendMessage(
        id: String,
        message: String
    ) {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.chatSendMessageState },
                set = { state -> copy(chatSendMessageState = state) }
            ),
            block = {
                chatSendMessageUseCase(
                    ChatMessageSendRequest(
                        id = id,
                        message = message
                    )
                )
            }
        )
    }

    fun doReadAllMessage(
        id: String,
        message: String
    ) {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.chatReadAllMessageState },
                set = { state -> copy(chatReadAllMessageState = state) }
            ),
            block = {
                chatMessageMarkAsReadUseCase(
                    ChatMessageMarkAsReadRequest(
                        id = id,
                        message = message
                    )
                )
            }
        )
    }

}