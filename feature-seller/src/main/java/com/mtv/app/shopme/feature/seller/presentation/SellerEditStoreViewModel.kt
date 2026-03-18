/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerEditStoreViewModel.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 15.50
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.based.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SellerEditStoreViewModel @Inject constructor(

) : BaseViewModel(), UiOwner<SellerEditStoreStateListener, SellerEditStoreDataListener> {

    override val uiState = MutableStateFlow(
        SellerEditStoreStateListener()
    )

    override val uiData = MutableStateFlow(
        SellerEditStoreDataListener(
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

    fun onStoreNameChange(value: String) {
        uiData.value = uiData.value.copy(storeName = value)
    }


    fun onPhoneChange(value: String) {
        uiData.value = uiData.value.copy(phone = value)
    }

    fun onDescriptionChange(value: String) {
        uiData.value = uiData.value.copy(description = value)
    }

    fun onVillageChange(value: String) {
        uiData.value = uiData.value.copy(village = value)
    }

    fun onBlockChange(value: String) {
        uiData.value = uiData.value.copy(block = value)
    }

    fun onNumberChange(value: String) {
        uiData.value = uiData.value.copy(number = value)
    }

    fun onRtChange(value: String) {
        uiData.value = uiData.value.copy(rt = value)
    }

    fun onRwChange(value: String) {
        uiData.value = uiData.value.copy(rw = value)
    }

    fun onUploadPhoto() {
        // open gallery / camera
    }

    fun saveStore() {

    }
}