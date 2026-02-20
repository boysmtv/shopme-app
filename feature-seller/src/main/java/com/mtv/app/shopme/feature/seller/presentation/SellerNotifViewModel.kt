/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerNotifViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.27
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.google.protobuf.LazyStringArrayList.emptyList
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.shopme.common.ConstantPreferences.FCM_SELLER_NOTIFICATION
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.seller.contract.SellerNotifData
import com.mtv.app.shopme.feature.seller.contract.SellerNotifDialog
import com.mtv.app.shopme.feature.seller.contract.SellerNotifState
import com.mtv.app.shopme.feature.seller.model.SellerNotifItem
import com.mtv.based.core.network.utils.ResourceFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SellerNotifViewModel @Inject constructor(
    private val securePrefs: SecurePrefs,
) : BaseViewModel(), UiOwner<SellerNotifState, SellerNotifData> {

    override val uiState = MutableStateFlow(SellerNotifState())
    override val uiData = MutableStateFlow(SellerNotifData())

    init {
        getLocalNotification()
    }

    fun getLocalNotification() {
        val local = securePrefs.getObject(
            FCM_SELLER_NOTIFICATION,
            Array<SellerNotifItem>::class.java
        )?.toList() ?: emptyList()

//        uiData.update { it.copy(localNotification = local) }
        uiState.update {
            it.copy(notificationState = ResourceFirebase.Success(""))
        }
    }

    fun clearNotification() {
        securePrefs.remove(FCM_SELLER_NOTIFICATION)
//        uiData.update { it.copy(localNotification = emptyList()) }
        uiState.update { it.copy(activeDialog = SellerNotifDialog.Success) }
    }

    fun dismissDialog() {
        uiState.update { it.copy(activeDialog = null) }
    }
}