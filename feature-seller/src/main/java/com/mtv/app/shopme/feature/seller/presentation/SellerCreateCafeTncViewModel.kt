/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeTncViewModel.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 13.10
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.based.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeTncStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SellerCreateCafeTncViewModel @Inject constructor(

) : BaseViewModel(),
    UiOwner<SellerCreateCafeTncStateListener, SellerCreateCafeTncDataListener> {

    override val uiState = MutableStateFlow(
        SellerCreateCafeTncStateListener()
    )

    override val uiData = MutableStateFlow(
        SellerCreateCafeTncDataListener()
    )

    fun onAgreeTerms(value: Boolean) {
        uiData.value = uiData.value.copy(agreeTerms = value)
    }

    fun onAgreeFoodSafety(value: Boolean) {
        uiData.value = uiData.value.copy(agreeFoodSafety = value)
    }

    fun onAgreeLocation(value: Boolean) {
        uiData.value = uiData.value.copy(agreeLocation = value)
    }

    fun isValid(): Boolean {

        val data = uiData.value

        return data.agreeTerms &&
                data.agreeFoodSafety &&
                data.agreeLocation
    }

    fun next() {

        if (!isValid()) return

        // navigate next step create cafe
    }
}