/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerPaymentMethodViewModel.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 22.19
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.seller.contract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SellerPaymentMethodViewModel @Inject constructor() :
    BaseViewModel(),
    UiOwner<SellerPaymentMethodStateListener, SellerPaymentMethodDataListener> {

    override val uiState = MutableStateFlow(
        SellerPaymentMethodStateListener()
    )

    override val uiData = MutableStateFlow(
        SellerPaymentMethodDataListener()
    )

    fun toggleCash(enabled: Boolean) {
        uiData.value = uiData.value.copy(cashEnabled = enabled)
    }

    fun onBankChange(value: String) {
        uiData.value = uiData.value.copy(bankNumber = value)
    }

    fun onGopayChange(value: String) {
        uiData.value = uiData.value.copy(gopayNumber = value)
    }

    fun onDanaChange(value: String) {
        uiData.value = uiData.value.copy(danaNumber = value)
    }

    fun onOvoChange(value: String) {
        uiData.value = uiData.value.copy(ovoNumber = value)
    }

    fun save() {
        // API CALL
    }
}