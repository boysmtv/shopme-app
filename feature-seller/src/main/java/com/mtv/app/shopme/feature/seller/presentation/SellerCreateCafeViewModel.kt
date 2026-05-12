/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeViewModel.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 14.12
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.Village
import com.mtv.app.shopme.domain.param.CafeAddParam
import com.mtv.app.shopme.domain.param.CafeAddressUpsertParam
import com.mtv.app.shopme.domain.usecase.CreateCafeUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.domain.usecase.GetVillageUseCase
import com.mtv.app.shopme.domain.usecase.UploadMediaUseCase
import com.mtv.app.shopme.domain.usecase.UpsertCafeAddressUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeEffect
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeEvent
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SessionManager
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SellerCreateCafeViewModel @Inject constructor(
    private val createCafeUseCase: CreateCafeUseCase,
    private val getSellerProfileUseCase: GetSellerProfileUseCase,
    private val getVillageUseCase: GetVillageUseCase,
    private val upsertCafeAddressUseCase: UpsertCafeAddressUseCase,
    private val uploadMediaUseCase: UploadMediaUseCase,
    private val sessionManager: SessionManager
) : BaseEventViewModel<SellerCreateCafeEvent, SellerCreateCafeEffect>() {

    private val _state = MutableStateFlow(SellerCreateCafeUiState())
    val uiState = _state.asStateFlow()
    private var villages: List<Village> = emptyList()

    override fun onEvent(event: SellerCreateCafeEvent) {
        when (event) {

            is SellerCreateCafeEvent.Load -> load()
            is SellerCreateCafeEvent.DismissDialog -> clearDialog()

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

    private fun load() {
        observeDataFlow(
            flow = getVillageUseCase(),
            onState = { state ->
                _state.update { it.copy(isLoading = state is LoadState.Loading) }
                if (state is LoadState.Success) {
                    villages = state.data
                    _state.update { it.copy(isLoading = false) }
                }
            },
            onError = ::showError
        )
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
        val state = _state.value
        val villageId = resolveVillageId(state.village) ?: run {
            showError(UiError.Validation(message = "Village harus sesuai data backend"))
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val imageReference = runCatching {
                resolveUploadedReference(state.cafePhoto, "cafes")
            }.getOrElse {
                _state.update { it.copy(isLoading = false) }
                showError(UiError.Validation(message = it.message ?: "Failed uploading image"))
                return@launch
            }

            val cafeRequest = CafeAddParam(
                name = state.cafeName,
                phone = state.phone,
                description = state.description,
                minimalOrder = state.minOrder,
                openTime = state.openHours,
                closeTime = state.closeHours,
                image = imageReference.orEmpty(),
                isActive = true
            )

            observeDataFlow(
                flow = createCafeUseCase(cafeRequest),
                onState = { createState ->
                    _state.update { it.copy(isLoading = createState is LoadState.Loading) }
                    if (createState is LoadState.Success) {
                        attachAddress(villageId)
                    }
                },
                onError = ::showError
            )
        }
    }

    private suspend fun resolveUploadedReference(reference: String?, scope: String): String? {
        if (reference.isNullOrBlank() || !reference.startsWith("content://")) {
            return reference
        }

        return when (val result = uploadMediaUseCase(reference, scope).first { it !is Resource.Loading }) {
            is Resource.Success -> result.data.originalUrl
            is Resource.Error -> throw IllegalStateException(result.error.message)
            else -> reference
        }
    }

    private fun attachAddress(villageId: String) {
        observeDataFlow(
            flow = getSellerProfileUseCase(),
            onState = { profileState ->
                if (profileState is LoadState.Success) {
                    val cafeId = profileState.data.cafeId
                    if (cafeId.isNullOrBlank()) {
                        _state.update { it.copy(isLoading = false) }
                        showError(UiError.Unknown(message = "Cafe seller belum tersedia"))
                    } else {
                        upsertAddress(cafeId, villageId)
                    }
                } else {
                    _state.update { it.copy(isLoading = profileState is LoadState.Loading) }
                }
            },
            onError = ::showError
        )
    }

    private fun upsertAddress(cafeId: String, villageId: String) {
        val state = _state.value
        observeDataFlow(
            flow = upsertCafeAddressUseCase(
                CafeAddressUpsertParam(
                    cafeId = cafeId,
                    villageId = villageId,
                    block = state.block,
                    number = state.number,
                    rt = state.rt,
                    rw = state.rw
                )
            ),
            onState = { addressState ->
                _state.update { it.copy(isLoading = addressState is LoadState.Loading) }
                if (addressState is LoadState.Success) {
                    emitEffect(SellerCreateCafeEffect.NavigateSuccess)
                }
            },
            onError = ::showError
        )
    }

    private fun resolveVillageId(name: String): String? {
        return villages.firstOrNull { it.name.equals(name.trim(), ignoreCase = true) }?.id
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
                    onPrimary = { clearDialog() }
                )
            )
        }
    }

    private fun showError(message: String) {
        showError(UiError.Validation(message = message))
    }

    private fun clearDialog() {
        super.dismissDialog()
        _state.update { it.copy(isLoading = false) }
    }
}
