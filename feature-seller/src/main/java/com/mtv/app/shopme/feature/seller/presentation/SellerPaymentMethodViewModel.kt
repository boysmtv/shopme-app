/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerPaymentMethodViewModel.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 22.19
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SellerPaymentMethodViewModel @Inject constructor(

) : BaseEventViewModel<SellerPaymentMethodEvent, SellerPaymentMethodEffect>() {

    private val _state = MutableStateFlow(SellerPaymentMethodUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerPaymentMethodEvent) {
        when (event) {

            SellerPaymentMethodEvent.Load -> {}

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

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            delay(1000) // simulate API

            _state.update { it.copy(isLoading = false) }

            emitEffect(SellerPaymentMethodEffect.SaveSuccess)
        }
    }

    private fun showError(message: String) {
        setDialog(
            UiDialog.Center(
                state = DialogStateV1(
                    type = DialogType.ERROR,
                    title = "Error",
                    message = message
                ),
                onPrimary = { dismissDialog() }
            )
        )
    }
}