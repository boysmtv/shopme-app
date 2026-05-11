package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.usecase.ConfirmOrderTransferUseCase
import com.mtv.app.shopme.domain.usecase.GetOrderDetailUseCase
import com.mtv.app.shopme.feature.customer.contract.OrderDetailEffect
import com.mtv.app.shopme.feature.customer.contract.OrderDetailEvent
import com.mtv.app.shopme.feature.customer.contract.OrderDetailUiState
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
class OrderDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getOrderDetailUseCase: GetOrderDetailUseCase,
    private val confirmOrderTransferUseCase: ConfirmOrderTransferUseCase,
    private val sessionManager: SessionManager
) : BaseEventViewModel<OrderDetailEvent, OrderDetailEffect>() {

    private val orderId: String = checkNotNull(savedStateHandle["orderId"])
    private val _state = MutableStateFlow(OrderDetailUiState(orderId = orderId))
    val uiState = _state.asStateFlow()

    override fun onEvent(event: OrderDetailEvent) {
        when (event) {
            OrderDetailEvent.Load -> load()
            OrderDetailEvent.DismissDialog -> dismissDialog()
            OrderDetailEvent.ClickBack -> emitEffect(OrderDetailEffect.NavigateBack)
            OrderDetailEvent.ClickChat -> emitEffect(OrderDetailEffect.NavigateToChat)
            OrderDetailEvent.ConfirmTransfer -> confirmTransfer()
        }
    }

    private fun load() {
        observeDataFlow(
            flow = getOrderDetailUseCase(orderId),
            onState = { state ->
                _state.update {
                    if (state is LoadState.Success) {
                        it.copy(isLoading = false, order = state.data)
                    } else {
                        it.copy(isLoading = state is LoadState.Loading)
                    }
                }
            },
            onError = { showError(it) }
        )
    }

    private fun confirmTransfer() {
        observeDataFlow(
            flow = confirmOrderTransferUseCase(orderId),
            onState = { state ->
                _state.update { it.copy(isLoading = state is LoadState.Loading) }
            },
            onSuccess = { load() },
            onError = { showError(it) }
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
