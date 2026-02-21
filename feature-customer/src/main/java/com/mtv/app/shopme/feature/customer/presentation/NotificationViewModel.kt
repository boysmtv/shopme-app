/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotificationViewModel.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.26
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.customer.contract.NotificationDataListener
import com.mtv.app.shopme.feature.customer.contract.NotificationStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class NotificationViewModel @Inject constructor() :
    BaseViewModel(),
    UiOwner<NotificationStateListener, NotificationDataListener> {

    override val uiState = MutableStateFlow(NotificationStateListener())
    override val uiData = MutableStateFlow(NotificationDataListener())

    fun toggleOrder(value: Boolean) {
        uiData.value = uiData.value.copy(orderNotification = value)
    }

    fun togglePromo(value: Boolean) {
        uiData.value = uiData.value.copy(promoNotification = value)
    }

    fun toggleChat(value: Boolean) {
        uiData.value = uiData.value.copy(chatNotification = value)
    }

    fun togglePush(value: Boolean) {
        uiData.value = uiData.value.copy(pushEnabled = value)
    }

    fun toggleEmail(value: Boolean) {
        uiData.value = uiData.value.copy(emailEnabled = value)
    }
}