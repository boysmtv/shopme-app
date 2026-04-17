/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderDetailViewModel.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 13.04
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.based.core.provider.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SellerOrderDetailViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
) : BaseEventViewModel<SellerOrderDetailEvent, SellerOrderDetailEffect>() {

    private val orderId: String = checkNotNull(savedStateHandle["orderId"])

    private val _state = MutableStateFlow(
        SellerOrderDetailUiState(orderId = orderId)
    )
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerOrderDetailEvent) {
        when (event) {

            SellerOrderDetailEvent.Load -> load()

            SellerOrderDetailEvent.DismissDialog -> dismissDialog()

            is SellerOrderDetailEvent.ChangeStatus ->
                update { copy(currentStatus = event.status) }

            SellerOrderDetailEvent.SaveStatus -> saveStatus()

            SellerOrderDetailEvent.ClickBack ->
                emitEffect(SellerOrderDetailEffect.NavigateBack)
        }
    }

    private fun update(block: SellerOrderDetailUiState.() -> SellerOrderDetailUiState) {
        _state.update { it.block() }
    }

    private fun load() {
        // TODO: get order detail API
        _state.update {
            it.copy(
                customerName = "John Doe",
                total = "50000"
            )
        }
    }

    private fun saveStatus() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(1000)
            _state.update { it.copy(isLoading = false) }
            emitEffect(SellerOrderDetailEffect.UpdateSuccess)
        }
    }
}