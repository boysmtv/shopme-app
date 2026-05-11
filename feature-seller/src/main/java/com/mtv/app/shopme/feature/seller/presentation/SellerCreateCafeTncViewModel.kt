/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeTncViewModel.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 13.10
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.SupportCenter
import com.mtv.app.shopme.domain.usecase.GetSupportCenterUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncEffect
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncEvent
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncItemUiState
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncUiState
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
class SellerCreateCafeTncViewModel @Inject constructor(
    private val getSupportCenterUseCase: GetSupportCenterUseCase,
    private val sessionManager: SessionManager
) : BaseEventViewModel<SellerCreateCafeTncEvent, SellerCreateCafeTncEffect>() {

    private val _state = MutableStateFlow(SellerCreateCafeTncUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerCreateCafeTncEvent) {
        when (event) {
            is SellerCreateCafeTncEvent.Load -> load()
            is SellerCreateCafeTncEvent.DismissDialog -> dismissDialog()
            is SellerCreateCafeTncEvent.ToggleTerm -> toggleTerm(event.id, event.value)
            SellerCreateCafeTncEvent.Next -> next()
            SellerCreateCafeTncEvent.ClickBack ->
                emitEffect(SellerCreateCafeTncEffect.NavigateBack)
        }
    }

    private fun update(block: SellerCreateCafeTncUiState.() -> SellerCreateCafeTncUiState) {
        _state.update { it.block() }
    }

    private fun load() {
        observeDataFlow(
            flow = getSupportCenterUseCase(),
            onState = { state ->
                _state.update { current ->
                    when (state) {
                        is LoadState.Success -> state.data.toUiState()
                        else -> current.copy(isLoading = state is LoadState.Loading)
                    }
                }
            },
            onError = ::showError
        )
    }

    private fun toggleTerm(id: String, value: Boolean) {
        update {
            copy(
                terms = terms.map { item ->
                    if (item.id == id) item.copy(checked = value) else item
                }
            )
        }
    }

    private fun next() {
        val state = _state.value

        if (!isValid(state)) return

        emitEffect(SellerCreateCafeTncEffect.NavigateNext)
    }

    private fun isValid(state: SellerCreateCafeTncUiState): Boolean {
        return state.terms.isNotEmpty() && state.terms.all { it.checked }
    }

    private fun SupportCenter.toUiState() = SellerCreateCafeTncUiState(
        isLoading = false,
        title = sellerOnboardingTitle,
        description = sellerOnboardingDescription,
        footer = sellerOnboardingFooter,
        terms = sellerTerms.map {
            SellerCreateCafeTncItemUiState(
                id = it.id,
                title = it.title,
                description = it.description
            )
        }
    )

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
