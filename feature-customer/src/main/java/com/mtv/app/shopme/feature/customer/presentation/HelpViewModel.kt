/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HelpViewModel.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 10.26
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.usecase.GetSupportCenterUseCase
import com.mtv.app.shopme.feature.customer.contract.HelpEffect
import com.mtv.app.shopme.feature.customer.contract.HelpEvent
import com.mtv.app.shopme.feature.customer.contract.HelpFaq
import com.mtv.app.shopme.feature.customer.contract.HelpUiState
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
class HelpViewModel @Inject constructor(
    private val getSupportCenterUseCase: GetSupportCenterUseCase,
    private val sessionManager: SessionManager
) :
    BaseEventViewModel<HelpEvent, HelpEffect>() {

    private val _state = MutableStateFlow(HelpUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: HelpEvent) {
        when (event) {
            is HelpEvent.Load -> load()
            is HelpEvent.DismissDialog -> dismissDialog()
            is HelpEvent.Refresh -> load()
            is HelpEvent.ToggleFaq -> toggleFaq(event.faq)
            is HelpEvent.ClickBack -> emitEffect(HelpEffect.NavigateBack)
            is HelpEvent.ClickAbout -> emitEffect(HelpEffect.NavigateAbout)
            is HelpEvent.ClickPrivacy -> emitEffect(HelpEffect.NavigatePrivacy)
            is HelpEvent.ClickShipping -> emitEffect(HelpEffect.NavigateShipping)
            is HelpEvent.ClickPayment -> emitEffect(HelpEffect.NavigatePayment)
            is HelpEvent.ClickContact -> emitEffect(HelpEffect.NavigateContact)
        }
    }

    private fun load() {
        observeDataFlow(
            flow = getSupportCenterUseCase(),
            onState = { state ->
                _state.update { current ->
                    when (state) {
                        is LoadState.Success -> HelpUiState(
                            faq = state.data.faq.map {
                                HelpFaq(
                                    question = it.question,
                                    answer = it.answer
                                )
                            },
                            isLoading = false
                        )

                        else -> current.copy(isLoading = state is LoadState.Loading)
                    }
                }
            },
            onError = ::showError
        )
    }

    private fun toggleFaq(target: HelpFaq) {
        _state.update { current ->
            current.copy(
                faq = current.faq.map {
                    when {
                        it == target -> it.copy(expanded = !it.expanded)
                        else -> it.copy(expanded = false)
                    }
                }
            )
        }
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
