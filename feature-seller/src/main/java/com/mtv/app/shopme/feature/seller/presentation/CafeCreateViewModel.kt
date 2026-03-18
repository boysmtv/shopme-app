/*
 * Project: Boys.mtv@gmail.com
 * File: CafeCreateViewModel.kt
 *
 * Last modified by Dedy Wijaya on 13/03/2026 13.41
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.based.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.data.remote.request.CafeAddRequest
import com.mtv.app.shopme.feature.seller.contract.CafeCreateDataListener
import com.mtv.app.shopme.feature.seller.contract.CafeCreateStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CafeCreateViewModel @Inject constructor(

) : BaseViewModel(),
    UiOwner<CafeCreateStateListener, CafeCreateDataListener> {

    override val uiState = MutableStateFlow(
        CafeCreateStateListener()
    )

    override val uiData = MutableStateFlow(
        CafeCreateDataListener()
    )

    fun onNameChange(value: String) {
        uiData.value = uiData.value.copy(name = value)
    }

    fun onPhoneChange(value: String) {
        uiData.value = uiData.value.copy(phone = value)
    }

    fun onDescriptionChange(value: String) {
        uiData.value = uiData.value.copy(description = value)
    }

    fun onMinimalOrderChange(value: String) {
        uiData.value = uiData.value.copy(minimalOrder = value)
    }

    fun onOpenTimeChange(value: String) {
        uiData.value = uiData.value.copy(openTime = value)
    }

    fun onCloseTimeChange(value: String) {
        uiData.value = uiData.value.copy(closeTime = value)
    }

    fun onUploadImage() {
        // open gallery or camera
    }

    fun createCafe() {
        val request = CafeAddRequest(
            name = uiData.value.name,
            phone = uiData.value.phone,
            description = uiData.value.description,
            minimalOrder = uiData.value.minimalOrder,
            openTime = uiData.value.openTime,
            closeTime = uiData.value.closeTime,
            image = uiData.value.image ?: "",
            isActive = uiData.value.isActive
        )

        // TODO call repository
    }
}