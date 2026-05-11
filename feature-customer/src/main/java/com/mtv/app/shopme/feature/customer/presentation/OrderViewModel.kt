/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: OrderViewModel.kt
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.usecase.ConfirmOrderTransferUseCase
import com.mtv.app.shopme.domain.usecase.EnsureChatConversationUseCase
import com.mtv.app.shopme.domain.usecase.GetOrdersUseCase
import com.mtv.app.shopme.feature.customer.contract.OrderEffect
import com.mtv.app.shopme.feature.customer.contract.OrderEvent
import com.mtv.app.shopme.feature.customer.contract.OrderUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SessionManager
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val confirmOrderTransferUseCase: ConfirmOrderTransferUseCase,
    private val ensureChatConversationUseCase: EnsureChatConversationUseCase,
    private val sessionManager: SessionManager
) : BaseEventViewModel<OrderEvent, OrderEffect>() {

    private val _state = MutableStateFlow(OrderUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: OrderEvent) {
        when (event) {
            is OrderEvent.Load -> loadOrders()
            is OrderEvent.DismissDialog -> dismissDialog()
            is OrderEvent.Reload -> loadOrders()
            is OrderEvent.ClickOrder -> emitEffect(OrderEffect.NavigateToDetail(event.orderId))
            is OrderEvent.ConfirmTransfer -> confirmTransfer(event.orderId)
            is OrderEvent.ClickChatList -> emitEffect(OrderEffect.NavigateToChatList)
            is OrderEvent.ClickChat -> openChat(event.cafeId)
            is OrderEvent.ClickBack -> emitEffect(OrderEffect.NavigateBack)
        }
    }

    private fun openChat(cafeId: String) {
        if (cafeId.isBlank()) return
        observeDataFlow(
            flow = ensureChatConversationUseCase(cafeId),
            onSuccess = { emitEffect(OrderEffect.NavigateToChat(it)) },
            onError = { error ->
                _state.update { it.copy(isLoading = false) }
                showError(error)
            }
        )
    }

    private fun loadOrders() {
        observeDataFlow(
            flow = getOrdersUseCase(),
            onState = { result ->
                _state.update {
                    it.copy(
                        isLoading = result is LoadState.Loading,
                        orders = if (result is LoadState.Success) result.data else it.orders
                    )
                }
            },
            onError = { error ->
                _state.update { it.copy(isLoading = false) }
                showError(error)
            }
        )
    }

    private fun confirmTransfer(orderId: String) {
        observeDataFlow(
            flow = confirmOrderTransferUseCase(orderId),
            onState = { state ->
                _state.update { it.copy(isLoading = state is LoadState.Loading) }
            },
            onSuccess = { loadOrders() },
            onError = { error ->
                _state.update { it.copy(isLoading = false) }
                showError(error)
            }
        )
    }

    private fun showError(error: UiError) {
        handleSessionError(
            error = error,
            sessionManager = sessionManager,
            beforeLogout = { _state.update { it.copy(isLoading = false) } }
        ) {
            setDialog(
                UiDialog.Center(
                    state = DialogStateV1(
                        type = DialogType.ERROR,
                        title = ErrorMessages.GENERIC_ERROR,
                        message = it.message
                    ),
                    onPrimary = { dismissDialog() }
                )
            )
        }
    }
}
