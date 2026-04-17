/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerDashboardViewModel.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.15
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.SellerOrderItem
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEffect
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEffect.*
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEvent
import com.mtv.app.shopme.feature.seller.contract.SellerDashboardUiState
import com.mtv.based.core.provider.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SellerDashboardViewModel @Inject constructor(
    private val sessionManager: SessionManager
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

            is SellerDashboardEvent.ChangeFilter -> TODO()
            is SellerDashboardEvent.ChangeSort -> TODO()
            SellerDashboardEvent.ToggleOnline -> TODO()
        }
    }

    private fun load() {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(1000)
            _state.update { it.copy(isLoading = false) }
        }

        _state.update {
            it.copy(
                isLoading = false,
                orders = mockOrders()
            )
        }
    }

    private fun mockOrders(): List<SellerOrderItem> = listOf(
        SellerOrderItem(
            id = "1",
            invoice = "INV-2026-001",
            customer = "John Doe",
            total = "Rp 120.000",
            date = "18 Feb 2026",
            time = "09:45",
            paymentMethod = "Transfer Bank",
            status = "Pending",
            location = "Puri Lestari - Blok H12/12"
        ),
        SellerOrderItem(
            id = "2",
            invoice = "INV-2026-002",
            customer = "Jane Smith",
            total = "Rp 250.000",
            date = "17 Feb 2026",
            time = "14:30",
            paymentMethod = "E-Wallet",
            status = "Completed",
            location = "Puri Lestari - Blok H11/10"
        )
    )
}