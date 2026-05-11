/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderViewModel.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 13.21
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.SellerOrderItem
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerOrdersUseCase
import com.mtv.app.shopme.domain.usecase.UpdateSellerAvailabilityUseCase
import com.mtv.app.shopme.feature.seller.contract.*
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
class SellerOrderViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val getSellerOrdersUseCase: GetSellerOrdersUseCase,
    private val getSellerProfileUseCase: GetSellerProfileUseCase,
    private val updateSellerAvailabilityUseCase: UpdateSellerAvailabilityUseCase
) : BaseEventViewModel<SellerOrderEvent, SellerOrderEffect>() {

    private val _state = MutableStateFlow(SellerOrderUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerOrderEvent) {
        when (event) {

            SellerOrderEvent.Load -> load()

            SellerOrderEvent.DismissDialog -> dismissDialog()

            is SellerOrderEvent.SelectFilter ->
                update { copy(selectedFilter = event.value) }

            SellerOrderEvent.ToggleOnline ->
                toggleOnline()

            is SellerOrderEvent.ClickOrder ->
                emitEffect(SellerOrderEffect.NavigateToOrderDetail(event.orderId))
        }
    }

    private fun update(block: SellerOrderUiState.() -> SellerOrderUiState) {
        _state.update { it.block() }
    }

    private fun load() {
        observeProfile()
        observeIndependentDataFlow(
            flow = getSellerOrdersUseCase(),
            onState = { result ->
                _state.update {
                    it.copy(
                        isLoading = result is LoadState.Loading,
                        orders = if (result is LoadState.Success) {
                            result.data.map { sellerOrder ->
                                sellerOrder.toOrderSummary()
                            }
                        } else {
                            it.orders
                        }
                    )
                }
            },
            onError = ::showError
        )
    }

    private fun observeProfile() {
        observeIndependentDataFlow(
            flow = getSellerProfileUseCase(),
            onState = { result ->
                _state.update {
                    val profile = (result as? LoadState.Success)?.data
                    it.copy(isOnline = profile?.isOnline ?: it.isOnline)
                }
            },
            onError = ::showError
        )
    }

    private fun toggleOnline() {
        observeDataFlow(
            flow = updateSellerAvailabilityUseCase(!_state.value.isOnline),
            onState = { result ->
                _state.update {
                    val profile = (result as? LoadState.Success)?.data
                    it.copy(
                        isLoading = result is LoadState.Loading || it.isLoading,
                        isOnline = profile?.isOnline ?: it.isOnline
                    )
                }
            },
            onError = ::showError
        )
    }

    private fun SellerOrderItem.toOrderSummary(): OrderSummary {
        return OrderSummary(
            orderId = id,
            customerName = customer,
            total = total,
            date = date,
            time = time,
            paymentMethod = paymentMethod,
            status = status,
            location = location
        )
    }

    private fun showError(error: UiError) {
        handleSessionError(
            error = error,
            sessionManager = sessionManager,
            beforeLogout = { update { copy(isLoading = false) } }
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
