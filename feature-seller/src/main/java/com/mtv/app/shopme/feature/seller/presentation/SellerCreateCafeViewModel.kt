/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeViewModel.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 14.12
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.based.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SellerCreateCafeViewModel @Inject constructor(

) : BaseViewModel(),
    UiOwner<SellerCreateCafeStateListener, SellerCreateCafeDataListener> {

    override val uiState = MutableStateFlow(
        SellerCreateCafeStateListener()
    )

    override val uiData = MutableStateFlow(
        SellerCreateCafeDataListener()
    )

    fun onCafeNameChange(value: String) {
        uiData.update { it.copy(cafeName = value) }
    }

    fun onPhoneChange(value: String) {
        uiData.update { it.copy(phone = value) }
    }

    fun onMinOrderChange(value: String) {
        uiData.update { it.copy(minOrder = value) }
    }

    fun onOpenHoursChange(value: String) {
        uiData.update { it.copy(openHours = value) }
    }

    fun onDescriptionChange(value: String) {
        uiData.update { it.copy(description = value) }
    }

    fun onVillageChange(value: String) {
        uiData.update { it.copy(village = value) }
    }

    fun onBlockChange(value: String) {
        uiData.update { it.copy(block = value) }
    }

    fun onNumberChange(value: String) {
        uiData.update { it.copy(number = value) }
    }

    fun onRtChange(value: String) {
        uiData.update { it.copy(rt = value) }
    }

    fun onRwChange(value: String) {
        uiData.update { it.copy(rw = value) }
    }

    fun onUploadPhoto() {
        // open image picker
    }

    fun onPickLocation() {
        // open google maps picker
    }

    fun onNextStep() {

        if (!validateStep()) return

        uiState.update {
            it.copy(step = it.step + 1)
        }
    }

    private fun validateStep(): Boolean {

        val data = uiData.value

        if (data.cafeName.isBlank()) {
            uiState.update { it.copy(errorMessage = "Cafe name is required") }
            return false
        }

        return true
    }

    fun createCafe() {
        // API create cafe
    }
}