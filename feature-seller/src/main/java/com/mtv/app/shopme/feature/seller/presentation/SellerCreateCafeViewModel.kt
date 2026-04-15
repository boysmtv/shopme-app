/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeViewModel.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 14.12
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeEffect
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeEvent
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeUiState
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
class SellerCreateCafeViewModel @Inject constructor(
) : BaseEventViewModel<SellerCreateCafeEvent, SellerCreateCafeEffect>() {

    private val _state = MutableStateFlow(SellerCreateCafeUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerCreateCafeEvent) {
        when (event) {

            is SellerCreateCafeEvent.Load -> {}
            is SellerCreateCafeEvent.DismissDialog -> dismissDialog()

            is SellerCreateCafeEvent.ChangeCafeName -> update { copy(cafeName = event.value) }
            is SellerCreateCafeEvent.ChangePhone -> update { copy(phone = event.value) }
            is SellerCreateCafeEvent.ChangeMinOrder -> update { copy(minOrder = event.value) }
            is SellerCreateCafeEvent.ChangeOpenHours -> update { copy(openHours = event.value) }
            is SellerCreateCafeEvent.ChangeDescription -> update { copy(description = event.value) }

            is SellerCreateCafeEvent.ChangeVillage -> update { copy(village = event.value) }
            is SellerCreateCafeEvent.ChangeBlock -> update { copy(block = event.value) }
            is SellerCreateCafeEvent.ChangeNumber -> update { copy(number = event.value) }
            is SellerCreateCafeEvent.ChangeRt -> update { copy(rt = event.value) }
            is SellerCreateCafeEvent.ChangeRw -> update { copy(rw = event.value) }

            SellerCreateCafeEvent.UploadPhoto -> emitEffect(SellerCreateCafeEffect.OpenImagePicker)
            SellerCreateCafeEvent.PickLocation -> emitEffect(SellerCreateCafeEffect.OpenLocationPicker)

            SellerCreateCafeEvent.NextStep -> nextStep()
            SellerCreateCafeEvent.CreateCafe -> createCafe()

            SellerCreateCafeEvent.ClickBack -> emitEffect(SellerCreateCafeEffect.NavigateBack)
            SellerCreateCafeEvent.PrevStep -> {
                _state.update { it.copy(step = (it.step - 1).coerceAtLeast(1)) }
            }
            is SellerCreateCafeEvent.ChangeCloseHours ->
                update { copy(closeHours = event.value) }
            is SellerCreateCafeEvent.ImageSelected ->
                update { copy(cafePhoto = event.uri) }

            SellerCreateCafeEvent.RemovePhoto ->
                update { copy(cafePhoto = null) }
        }
    }

    private fun update(block: SellerCreateCafeUiState.() -> SellerCreateCafeUiState) {
        _state.update { it.block() }
    }

    private fun nextStep() {
        val state = _state.value

        when (state.step) {
            1 -> {
                if (state.cafeName.isBlank() || state.phone.isBlank()) {
                    showError("Nama & Phone wajib diisi")
                    return
                }
            }

            2 -> {
                if (state.village.isBlank()) {
                    showError("Alamat belum lengkap")
                    return
                }
            }
        }

        _state.update {
            it.copy(step = (it.step + 1).coerceAtMost(4))
        }
    }

    private fun createCafe() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            delay(1000)

            _state.update { it.copy(isLoading = false) }

            emitEffect(SellerCreateCafeEffect.NavigateSuccess)
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