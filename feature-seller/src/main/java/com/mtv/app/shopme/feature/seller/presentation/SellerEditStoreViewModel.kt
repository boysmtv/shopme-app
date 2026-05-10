/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerEditStoreViewModel.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 15.50
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.Village
import com.mtv.app.shopme.domain.param.CafeAddParam
import com.mtv.app.shopme.domain.param.CafeAddressUpsertParam
import com.mtv.app.shopme.domain.usecase.GetCafeAddressUseCase
import com.mtv.app.shopme.domain.usecase.GetCafeUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.domain.usecase.GetVillageUseCase
import com.mtv.app.shopme.domain.usecase.UpsertCafeAddressUseCase
import com.mtv.app.shopme.domain.usecase.UpdateCafeUseCase
import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreEffect
import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreEvent
import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreUiState
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
class SellerEditStoreViewModel @Inject constructor(
    private val getSellerProfileUseCase: GetSellerProfileUseCase,
    private val getCafeUseCase: GetCafeUseCase,
    private val getCafeAddressUseCase: GetCafeAddressUseCase,
    private val updateCafeUseCase: UpdateCafeUseCase,
    private val upsertCafeAddressUseCase: UpsertCafeAddressUseCase,
    private val getVillageUseCase: GetVillageUseCase
) : BaseEventViewModel<SellerEditStoreEvent, SellerEditStoreEffect>() {

    private val _state = MutableStateFlow(SellerEditStoreUiState())
    val uiState = _state.asStateFlow()
    private var villages: List<Village> = emptyList()
    private var cafeId: String? = null
    private var closeTime: String = ""

    override fun onEvent(event: SellerEditStoreEvent) {
        when (event) {

            SellerEditStoreEvent.Load -> load()
            SellerEditStoreEvent.DismissDialog -> dismissDialog()

            // BASIC
            is SellerEditStoreEvent.ChangeStoreName ->
                update { copy(storeName = event.value) }

            is SellerEditStoreEvent.ChangePhone ->
                update { copy(phone = event.value) }

            is SellerEditStoreEvent.ChangeMinOrder ->
                update { copy(minOrder = event.value) }

            is SellerEditStoreEvent.ChangeStoreOpen ->
                update { copy(storeOpen = event.value) }

            is SellerEditStoreEvent.ChangeDescription ->
                update { copy(description = event.value) }

            // ADDRESS
            is SellerEditStoreEvent.ChangeVillage ->
                update { copy(village = event.value) }

            is SellerEditStoreEvent.ChangeBlock ->
                update { copy(block = event.value) }

            is SellerEditStoreEvent.ChangeNumber ->
                update { copy(number = event.value) }

            is SellerEditStoreEvent.ChangeRt ->
                update { copy(rt = event.value) }

            is SellerEditStoreEvent.ChangeRw ->
                update { copy(rw = event.value) }

            // TAB
            is SellerEditStoreEvent.ChangeTab ->
                update { copy(selectedTab = event.index) }

            // IMAGE
            SellerEditStoreEvent.UploadPhoto ->
                emitEffect(SellerEditStoreEffect.OpenImagePicker)

            is SellerEditStoreEvent.PhotoSelected ->
                update { copy(storePhoto = event.uri) }

            SellerEditStoreEvent.RemovePhoto ->
                update { copy(storePhoto = null) }

            // ACTION
            SellerEditStoreEvent.Save -> save()

            SellerEditStoreEvent.ClickBack ->
                emitEffect(SellerEditStoreEffect.NavigateBack)
        }
    }

    private fun update(block: SellerEditStoreUiState.() -> SellerEditStoreUiState) {
        _state.update { it.block() }
    }

    private fun load() {
        observeDataFlow(
            flow = getVillageUseCase(),
            onState = { state ->
                if (state is LoadState.Success) {
                    villages = state.data
                    loadCafe()
                } else {
                    _state.update { it.copy(isLoading = state is LoadState.Loading) }
                }
            },
            onError = ::showError
        )
    }

    private fun loadCafe() {
        observeDataFlow(
            flow = getSellerProfileUseCase(),
            onState = { profileState ->
                if (profileState is LoadState.Success) {
                    val sellerCafeId = profileState.data.cafeId
                    if (sellerCafeId.isNullOrBlank()) {
                        _state.update { it.copy(isLoading = false) }
                        showError(UiError.Unknown(message = "Cafe seller belum tersedia"))
                    } else {
                        cafeId = sellerCafeId
                        loadCafeDetail(sellerCafeId)
                        loadCafeAddress(sellerCafeId)
                    }
                } else {
                    _state.update { it.copy(isLoading = profileState is LoadState.Loading) }
                }
            },
            onError = ::showError
        )
    }

    private fun loadCafeDetail(id: String) {
        observeDataFlow(
            flow = getCafeUseCase(id),
            onState = { state ->
                _state.update {
                    if (state is LoadState.Success) {
                        closeTime = state.data.closeTime
                        it.copy(
                            isLoading = false,
                            storeName = state.data.name,
                            phone = state.data.phone,
                            minOrder = state.data.minimalOrder.stripTrailingZeros().toPlainString(),
                            storeOpen = state.data.openTime,
                            description = state.data.description,
                            storePhoto = state.data.image
                        )
                    } else {
                        it.copy(isLoading = state is LoadState.Loading)
                    }
                }
            },
            onError = ::showError
        )
    }

    private fun loadCafeAddress(id: String) {
        observeDataFlow(
            flow = getCafeAddressUseCase(id),
            onState = { state ->
                _state.update {
                    if (state is LoadState.Success) {
                        it.copy(
                            isLoading = false,
                            village = state.data.name,
                            block = state.data.block,
                            number = state.data.number,
                            rt = state.data.rt,
                            rw = state.data.rw
                        )
                    } else {
                        it.copy(isLoading = state is LoadState.Loading)
                    }
                }
            },
            onError = { _state.update { it.copy(isLoading = false) } }
        )
    }

    private fun save() {
        val state = _state.value

        if (state.storeName.isBlank() || state.phone.isBlank()) {
            showError("Store name & phone wajib diisi")
            return
        }

        val currentCafeId = cafeId
        if (currentCafeId.isNullOrBlank()) {
            showError("Cafe seller belum tersedia")
            return
        }

        val villageId = villages.firstOrNull { it.name.equals(state.village.trim(), ignoreCase = true) }?.id
        if (villageId.isNullOrBlank()) {
            showError("Village harus sesuai data backend")
            return
        }

        observeDataFlow(
            flow = updateCafeUseCase(
                currentCafeId,
                CafeAddParam(
                    name = state.storeName,
                    phone = state.phone,
                    description = state.description,
                    minimalOrder = state.minOrder,
                    openTime = state.storeOpen,
                    closeTime = closeTime.ifBlank { state.storeOpen },
                    image = state.storePhoto.orEmpty(),
                    isActive = true
                )
            ),
            onState = { updateState ->
                _state.update { it.copy(isLoading = updateState is LoadState.Loading) }
                if (updateState is LoadState.Success) {
                    saveAddress(currentCafeId, villageId)
                }
            },
            onError = ::showError
        )
    }

    private fun saveAddress(cafeId: String, villageId: String) {
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
                    emitEffect(SellerEditStoreEffect.SaveSuccess)
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
