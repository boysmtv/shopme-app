/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeTncViewModel.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 13.10
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncEffect
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncEvent
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SellerCreateCafeTncViewModel @Inject constructor(
) : BaseEventViewModel<SellerCreateCafeTncEvent, SellerCreateCafeTncEffect>() {

    private val _state = MutableStateFlow(SellerCreateCafeTncUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerCreateCafeTncEvent) {
        when (event) {
            is SellerCreateCafeTncEvent.Load -> {}
            is SellerCreateCafeTncEvent.DismissDialog -> dismissDialog()
            is SellerCreateCafeTncEvent.AgreeTerms -> update {
                copy(agreeTerms = event.value)
            }
            is SellerCreateCafeTncEvent.AgreeFoodSafety -> update {
                copy(agreeFoodSafety = event.value)
            }
            is SellerCreateCafeTncEvent.AgreeLocation -> update {
                copy(agreeLocation = event.value)
            }
            SellerCreateCafeTncEvent.Next -> next()
            SellerCreateCafeTncEvent.ClickBack ->
                emitEffect(SellerCreateCafeTncEffect.NavigateBack)
        }
    }

    private fun update(block: SellerCreateCafeTncUiState.() -> SellerCreateCafeTncUiState) {
        _state.update { it.block() }
    }

    private fun next() {
        val state = _state.value

        if (!isValid(state)) return

        emitEffect(SellerCreateCafeTncEffect.NavigateNext)
    }

    private fun isValid(state: SellerCreateCafeTncUiState): Boolean {
        return state.agreeTerms &&
                state.agreeFoodSafety &&
                state.agreeLocation
    }
}