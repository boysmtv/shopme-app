/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeViewModel.kt
 *
 * Last modified by Dedy Wijaya on 14/02/26 22.34
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.usecase.GetCafeUseCase
import com.mtv.app.shopme.domain.usecase.GetFoodsByCafeUseCase
import com.mtv.app.shopme.feature.customer.contract.CafeEffect
import com.mtv.app.shopme.feature.customer.contract.CafeEvent
import com.mtv.app.shopme.feature.customer.contract.CafeUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class CafeViewModel @Inject constructor(
    private val getCafeUseCase: GetCafeUseCase,
    private val getFoodsByCafeUseCase: GetFoodsByCafeUseCase,
    savedStateHandle: SavedStateHandle
) : BaseEventViewModel<CafeEvent, CafeEffect>() {

    private val _state = MutableStateFlow(CafeUiState())
    val uiState = _state.asStateFlow()

    private val cafeId: String = checkNotNull(savedStateHandle["cafeId"])

    override fun onEvent(event: CafeEvent) {
        when (event) {
            is CafeEvent.Load -> load()
            is CafeEvent.DismissDialog -> dismissDialog()
            is CafeEvent.ClickFood -> emitEffect(CafeEffect.NavigateToDetail(event.id))
            is CafeEvent.ClickBack -> emitEffect(CafeEffect.NavigateBack)
            is CafeEvent.ClickChat -> emitEffect(CafeEffect.NavigateToChat)
            is CafeEvent.ClickWhatsapp -> emitEffect(CafeEffect.NavigateToWhatsapp)
        }
    }

    private fun load() {
        observeCafe()
        observeFoods()
    }

    private fun observeCafe() {
        observeDataFlow(
            flow = getCafeUseCase(cafeId),
            onState = { state ->
                _state.update {
                    it.copy(cafe = state)
                }
            },
            onError = {
                showError(it)
            }
        )
    }

    private fun observeFoods() {
        observeDataFlow(
            flow = getFoodsByCafeUseCase(cafeId),
            onState = { state ->
                _state.update {
                    it.copy(foods = state)
                }
            },
            onError = {
                showError(it)
            }
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