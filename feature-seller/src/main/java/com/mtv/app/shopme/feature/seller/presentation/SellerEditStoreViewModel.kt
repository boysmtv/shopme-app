/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerEditStoreViewModel.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 15.50
 */

package com.mtv.app.shopme.feature.seller.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreEffect
import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreEvent
import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreUiState
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
class SellerEditStoreViewModel @Inject constructor(
) : BaseEventViewModel<SellerEditStoreEvent, SellerEditStoreEffect>() {

    private val _state = MutableStateFlow(
        SellerEditStoreUiState(
            storeName = "Shopme Store",
            phone = "08123456789",
            description = "Best shop for everything",
            village = "Griya Asri",
            block = "A",
            number = "12",
            rt = "01",
            rw = "02"
        )
    )
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerEditStoreEvent) {
        when (event) {

            SellerEditStoreEvent.Load -> {}
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

    private fun save() {
        val state = _state.value

        if (state.storeName.isBlank() || state.phone.isBlank()) {
            showError("Store name & phone wajib diisi")
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            delay(1000) // simulate API

            _state.update { it.copy(isLoading = false) }

            emitEffect(SellerEditStoreEffect.SaveSuccess)
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