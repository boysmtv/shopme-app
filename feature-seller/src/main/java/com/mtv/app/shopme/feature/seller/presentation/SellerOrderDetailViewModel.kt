/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderDetailViewModel.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 13.04
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.common.toRupiah
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.usecase.CancelSellerOrderUseCase
import com.mtv.app.shopme.domain.usecase.EnsureSellerChatConversationUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerOrderDetailUseCase
import com.mtv.app.shopme.domain.usecase.UpdateSellerOrderStatusUseCase
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.based.core.network.utils.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SellerOrderDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSellerOrderDetailUseCase: GetSellerOrderDetailUseCase,
    private val updateSellerOrderStatusUseCase: UpdateSellerOrderStatusUseCase,
    private val cancelSellerOrderUseCase: CancelSellerOrderUseCase,
    private val ensureSellerChatConversationUseCase: EnsureSellerChatConversationUseCase,
) : BaseEventViewModel<SellerOrderDetailEvent, SellerOrderDetailEffect>() {

    private val orderId: String = checkNotNull(savedStateHandle["orderId"])
    private val _state = MutableStateFlow(SellerOrderDetailUiState(orderId = orderId))
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerOrderDetailEvent) {
        when (event) {
            SellerOrderDetailEvent.Load -> load()
            SellerOrderDetailEvent.DismissDialog -> dismissDialog()
            is SellerOrderDetailEvent.ChangeStatus -> _state.update { it.copy(currentStatus = event.status) }
            SellerOrderDetailEvent.SaveStatus -> saveStatus()
            SellerOrderDetailEvent.ClickCancelOrder -> _state.update { it.copy(showCancelDialog = true) }
            SellerOrderDetailEvent.SubmitCancelOrder -> cancelOrder()
            SellerOrderDetailEvent.DismissCancelDialog -> {
                _state.update { it.copy(showCancelDialog = false, cancelReason = "") }
            }
            is SellerOrderDetailEvent.ChangeCancelReason -> {
                _state.update { it.copy(cancelReason = event.reason) }
            }
            SellerOrderDetailEvent.ClickChat -> openChat()
            SellerOrderDetailEvent.ClickBack -> emitEffect(SellerOrderDetailEffect.NavigateBack)
        }
    }

    private fun load() {
        observeDataFlow(
            flow = getSellerOrderDetailUseCase(orderId),
            onState = { state ->
                _state.update {
                    if (state is LoadState.Success) {
                        state.data.toUiState(it.currentStatus.takeIf { current -> current != state.data.status } ?: state.data.status)
                    } else {
                        it.copy(
                            isLoading = state is LoadState.Loading && it.items.isEmpty(),
                            isRefreshing = state is LoadState.Loading && it.items.isNotEmpty()
                        )
                    }
                }
            },
            onError = {}
        )
    }

    private fun saveStatus() {
        val targetStatus = _state.value.currentStatus
        observeDataFlow(
            flow = updateSellerOrderStatusUseCase(orderId, targetStatus),
            onState = { state ->
                _state.update { it.copy(isLoading = state is LoadState.Loading, isRefreshing = false) }
                if (state is LoadState.Success) emitEffect(SellerOrderDetailEffect.UpdateSuccess)
            },
            onError = {}
        )
    }

    private fun cancelOrder() {
        val reason = _state.value.cancelReason.trim()
        observeDataFlow(
            flow = cancelSellerOrderUseCase(orderId, reason.ifBlank { null }),
            onState = { state ->
                _state.update {
                    it.copy(
                        isLoading = state is LoadState.Loading,
                        isRefreshing = false,
                        showCancelDialog = state is LoadState.Loading
                    )
                }
            },
            onSuccess = {
                _state.update { it.copy(showCancelDialog = false, cancelReason = "") }
                load()
            },
            onError = {}
        )
    }

    private fun openChat() {
        observeDataFlow(
            flow = ensureSellerChatConversationUseCase(orderId),
            onState = { state ->
                _state.update { it.copy(isLoading = state is LoadState.Loading) }
                if (state is LoadState.Success) {
                    emitEffect(SellerOrderDetailEffect.NavigateToChat(state.data))
                }
            },
            onError = {}
        )
    }

    private fun Order.toUiState(status: com.mtv.app.shopme.domain.model.OrderStatus) = SellerOrderDetailUiState(
        isLoading = false,
        isRefreshing = false,
        orderId = id,
        currentStatus = status,
        customerName = customerName.ifBlank { customerId },
        customerAddress = deliveryAddress.ifBlank { cafeName },
        paymentMethod = paymentMethod.name,
        paymentStatus = paymentStatus.name,
        total = totalPrice.toRupiah(),
        items = items.map {
            SellerOrderLineItem(
                title = it.foodName.ifBlank { it.foodId },
                qty = it.quantity,
                price = it.price.toRupiah(),
                notes = it.notes.orEmpty()
            )
        },
        showCancelDialog = false,
        cancelReason = ""
    )
}
