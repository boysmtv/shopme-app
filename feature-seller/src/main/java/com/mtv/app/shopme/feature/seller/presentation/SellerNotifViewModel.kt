/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerNotifViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.27
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.common.ConstantPreferences.FCM_SELLER_NOTIFICATION
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.app.shopme.domain.model.SellerNotifItem
import com.mtv.based.core.network.utils.ResourceFirebase
import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SellerNotifViewModel @Inject constructor(
    private val securePrefs: SecurePrefs,
) : BaseEventViewModel<SellerNotifEvent, SellerNotifEffect>() {

    private val _state = MutableStateFlow(SellerNotifUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerNotifEvent) {
        when (event) {

            SellerNotifEvent.Load -> getLocalNotification()

            SellerNotifEvent.DismissDialog -> dismissDialog()

            SellerNotifEvent.GetNotification -> getLocalNotification()

            SellerNotifEvent.ClearNotification -> clearNotification()

            is SellerNotifEvent.ClickNotification -> {
                // nanti bisa navigate ke detail notif / order dll
            }

            SellerNotifEvent.ClickBack ->
                emitEffect(SellerNotifEffect.NavigateBack)
        }
    }

    private fun getLocalNotification() {
        val local = securePrefs.getObject(
            FCM_SELLER_NOTIFICATION,
            Array<SellerNotifItem>::class.java
        )?.toList() ?: emptyList()

        _state.update {
            it.copy(
                notifications = local,
                notificationState = ResourceFirebase.Success(EMPTY_STRING)
            )
        }
    }

    private fun clearNotification() {
        securePrefs.remove(FCM_SELLER_NOTIFICATION)

        _state.update {
            it.copy(
                notifications = emptyList(),
                activeDialog = SellerNotifDialog.Success
            )
        }
    }
}