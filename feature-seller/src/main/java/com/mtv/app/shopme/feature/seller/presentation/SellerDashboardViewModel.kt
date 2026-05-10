/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerDashboardViewModel.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.15
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerOrdersUseCase
import com.mtv.app.shopme.domain.usecase.UpdateSellerAvailabilityUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEffect
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEffect.*
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEvent
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardUiState
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
class SellerDashboardViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val getSellerOrdersUseCase: GetSellerOrdersUseCase,
    private val getSellerProfileUseCase: GetSellerProfileUseCase,
    private val updateSellerAvailabilityUseCase: UpdateSellerAvailabilityUseCase
) : BaseEventViewModel<SellerDashboardEvent, SellerDashboardEffect>() {

    private val _state = MutableStateFlow(SellerDashboardUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerDashboardEvent) {
        when (event) {
            SellerDashboardEvent.Load -> load()
            SellerDashboardEvent.DismissDialog -> dismissDialog()
            SellerDashboardEvent.Refresh -> refresh()
            SellerDashboardEvent.ClickProduct ->
                emitEffect(SellerDashboardEffect.NavigateToProduct)

            SellerDashboardEvent.ClickOrder ->
                emitEffect(SellerDashboardEffect.NavigateToOrder)

            is SellerDashboardEvent.ClickOrderDetail ->
                emitEffect(NavigateToOrderDetail(event.orderId))

            SellerDashboardEvent.ClickNotif ->
                emitEffect(SellerDashboardEffect.NavigateToNotif)

            is SellerDashboardEvent.ChangeFilter ->
                _state.update { it.copy(selectedFilter = event.value) }

            is SellerDashboardEvent.ChangeSort ->
                _state.update { it.copy(selectedSort = event.value) }

            SellerDashboardEvent.ToggleOnline ->
                toggleOnline()
        }
    }

    private fun load() {
        observeProfile()
        refresh()
    }

    private fun observeProfile() {
        observeDataFlow(
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

    private fun refresh() {
        observeDataFlow(
            flow = getSellerOrdersUseCase(),
            onState = { result ->
                _state.update {
                    it.copy(
                        isLoading = result is LoadState.Loading,
                        orders = if (result is LoadState.Success) result.data else it.orders
                    )
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
