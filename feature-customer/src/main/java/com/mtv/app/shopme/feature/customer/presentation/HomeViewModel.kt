/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.45
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.usecase.GetCustomerUseCase
import com.mtv.app.shopme.domain.usecase.GetFoodUseCase
import com.mtv.app.shopme.feature.customer.contract.HomeEffect
import com.mtv.app.shopme.feature.customer.contract.HomeEvent
import com.mtv.app.shopme.feature.customer.contract.HomeUiState
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
class HomeViewModel @Inject constructor(
    private val customerUseCase: GetCustomerUseCase,
    private val homeFoodUseCase: GetFoodUseCase,
) : BaseEventViewModel<HomeEvent, HomeEffect>() {

    private val _state = MutableStateFlow(HomeUiState())
    val uiState = _state.asStateFlow()
    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Load -> load()
            is HomeEvent.DismissDialog -> dismissDialog()
            is HomeEvent.ClickFood -> emitEffect(HomeEffect.NavigateToDetail(event.id))
            is HomeEvent.ClickSearch -> emitEffect(HomeEffect.NavigateToSearch)
            is HomeEvent.ClickNotif -> emitEffect(HomeEffect.NavigateToNotif)
        }
    }

    fun load() {
        observeCustomer()
        observeFoods()
    }

    private fun observeFoods() {
        observeDataFlow(
            flow = homeFoodUseCase(),
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

    private fun observeCustomer() {
        observeDataFlow(
            flow = customerUseCase(),
            onState = { state ->
                _state.update {
                    it.copy(customer = state)
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
                onPrimary = {
                    dismissDialog()
                }
            )
        )
    }

}