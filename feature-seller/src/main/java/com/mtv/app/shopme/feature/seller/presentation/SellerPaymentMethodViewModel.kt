/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerPaymentMethodViewModel.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 22.19
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.param.SellerPaymentMethodParam
import com.mtv.app.shopme.domain.usecase.GetSellerPaymentMethodsUseCase
import com.mtv.app.shopme.domain.usecase.UpdateSellerPaymentMethodsUseCase
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
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
class SellerPaymentMethodViewModel @Inject constructor(
    private val getSellerPaymentMethodsUseCase: GetSellerPaymentMethodsUseCase,
    private val updateSellerPaymentMethodsUseCase: UpdateSellerPaymentMethodsUseCase
) : BaseEventViewModel<SellerPaymentMethodEvent, SellerPaymentMethodEffect>() {

    private val _state = MutableStateFlow(SellerPaymentMethodUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerPaymentMethodEvent) {
        when (event) {

            SellerPaymentMethodEvent.Load -> load()

            SellerPaymentMethodEvent.DismissDialog -> dismissDialog()

            is SellerPaymentMethodEvent.ToggleCash ->
                update { copy(cashEnabled = event.value) }

            is SellerPaymentMethodEvent.ToggleBank ->
                update { copy(bankEnabled = event.value) }

            is SellerPaymentMethodEvent.ChangeBank ->
                update { copy(bankNumber = event.value) }

            is SellerPaymentMethodEvent.ToggleOvo ->
                update { copy(ovoEnabled = event.value) }

            is SellerPaymentMethodEvent.ChangeOvo ->
                update { copy(ovoNumber = event.value) }

            is SellerPaymentMethodEvent.ToggleDana ->
                update { copy(danaEnabled = event.value) }

            is SellerPaymentMethodEvent.ChangeDana ->
                update { copy(danaNumber = event.value) }

            is SellerPaymentMethodEvent.ToggleGopay ->
                update { copy(gopayEnabled = event.value) }

            is SellerPaymentMethodEvent.ChangeGopay ->
                update { copy(gopayNumber = event.value) }

            SellerPaymentMethodEvent.Save -> save()

            SellerPaymentMethodEvent.ClickBack ->
                emitEffect(SellerPaymentMethodEffect.NavigateBack)
        }
    }

    private fun update(block: SellerPaymentMethodUiState.() -> SellerPaymentMethodUiState) {
        _state.update { it.block() }
    }

    private fun save() {
        val state = _state.value

        if (state.bankEnabled && state.bankNumber.isBlank()) {
            showError("Nomor rekening wajib diisi")
            return
        }

        if (state.ovoEnabled && state.ovoNumber.isBlank()) {
            showError("Nomor OVO wajib diisi")
            return
        }

        if (state.danaEnabled && state.danaNumber.isBlank()) {
            showError("Nomor DANA wajib diisi")
            return
        }

        if (state.gopayEnabled && state.gopayNumber.isBlank()) {
            showError("Nomor GoPay wajib diisi")
            return
        }

        observeDataFlow(
            flow = updateSellerPaymentMethodsUseCase(
                SellerPaymentMethodParam(
                    cashEnabled = state.cashEnabled,
                    bankEnabled = state.bankEnabled,
                    bankNumber = state.bankNumber,
                    ovoEnabled = state.ovoEnabled,
                    ovoNumber = state.ovoNumber,
                    danaEnabled = state.danaEnabled,
                    danaNumber = state.danaNumber,
                    gopayEnabled = state.gopayEnabled,
                    gopayNumber = state.gopayNumber
                )
            ),
            onState = { result ->
                _state.update {
                    if (result is LoadState.Success) {
                        it.copy(
                            isLoading = false,
                            cashEnabled = result.data.cashEnabled,
                            bankEnabled = result.data.bankEnabled,
                            bankNumber = result.data.bankNumber,
                            ovoEnabled = result.data.ovoEnabled,
                            ovoNumber = result.data.ovoNumber,
                            danaEnabled = result.data.danaEnabled,
                            danaNumber = result.data.danaNumber,
                            gopayEnabled = result.data.gopayEnabled,
                            gopayNumber = result.data.gopayNumber
                        )
                    } else {
                        it.copy(isLoading = result is LoadState.Loading)
                    }
                }
                if (result is LoadState.Success) {
                    emitEffect(SellerPaymentMethodEffect.SaveSuccess)
                }
            },
            onError = ::showError
        )
    }

    private fun load() {
        observeDataFlow(
            flow = getSellerPaymentMethodsUseCase(),
            onState = { result ->
                _state.update {
                    if (result is LoadState.Success) {
                        it.copy(
                            isLoading = false,
                            cashEnabled = result.data.cashEnabled,
                            bankEnabled = result.data.bankEnabled,
                            bankNumber = result.data.bankNumber,
                            ovoEnabled = result.data.ovoEnabled,
                            ovoNumber = result.data.ovoNumber,
                            danaEnabled = result.data.danaEnabled,
                            danaNumber = result.data.danaNumber,
                            gopayEnabled = result.data.gopayEnabled,
                            gopayNumber = result.data.gopayNumber
                        )
                    } else {
                        it.copy(isLoading = result is LoadState.Loading)
                    }
                }
            },
            onError = ::showError
        )
    }

    private fun showError(message: String) {
        showError(UiError.Validation(message = message))
    }

    private fun showError(error: UiError) {
        _state.update { it.copy(isLoading = false) }
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
