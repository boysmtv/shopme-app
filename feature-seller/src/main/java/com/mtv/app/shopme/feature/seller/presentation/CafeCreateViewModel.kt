/*
 * Project: Boys.mtv@gmail.com
 * File: CafeCreateViewModel.kt
 *
 * Last modified by Dedy Wijaya on 13/03/2026 13.41
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.param.CafeAddParam
import com.mtv.app.shopme.feature.seller.contract.CafeCreateEffect
import com.mtv.app.shopme.feature.seller.contract.CafeCreateEvent
import com.mtv.app.shopme.feature.seller.contract.CafeCreateUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.UiError
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
class CafeCreateViewModel @Inject constructor(
) : BaseEventViewModel<CafeCreateEvent, CafeCreateEffect>() {

    private val _state = MutableStateFlow(CafeCreateUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: CafeCreateEvent) {
        when (event) {
            is CafeCreateEvent.Load -> {}
            is CafeCreateEvent.DismissDialog -> dismissDialog()

            is CafeCreateEvent.ChangeName -> update { copy(name = event.value) }
            is CafeCreateEvent.ChangePhone -> update { copy(phone = event.value) }
            is CafeCreateEvent.ChangeDescription -> update { copy(description = event.value) }
            is CafeCreateEvent.ChangeMinimalOrder -> update { copy(minimalOrder = event.value) }
            is CafeCreateEvent.ChangeOpenTime -> update { copy(openTime = event.value) }
            is CafeCreateEvent.ChangeCloseTime -> update { copy(closeTime = event.value) }

            is CafeCreateEvent.UploadImage -> emitEffect(CafeCreateEffect.OpenImagePicker)

            is CafeCreateEvent.Submit -> createCafe()

            is CafeCreateEvent.ClickBack -> emitEffect(CafeCreateEffect.NavigateBack)
        }
    }

    private fun update(block: CafeCreateUiState.() -> CafeCreateUiState) {
        _state.update { it.block() }
    }

    private fun createCafe() {
        val state = _state.value

        if (state.name.isBlank() || state.phone.isBlank()) {
            showError(UiError.Validation(message = "Nama & Phone wajib diisi"))
            return
        }

        val request = CafeAddParam(
            name = state.name,
            phone = state.phone,
            description = state.description,
            minimalOrder = state.minimalOrder,
            openTime = state.openTime,
            closeTime = state.closeTime,
            image = state.image ?: "",
            isActive = state.isActive
        )

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            delay(1000) // simulate API

            _state.update { it.copy(isLoading = false) }

            emitEffect(CafeCreateEffect.CreateSuccess)
        }
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