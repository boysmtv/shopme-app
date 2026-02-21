/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SecurityViewModel.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.11
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.customer.contract.SecurityDataListener
import com.mtv.app.shopme.feature.customer.contract.SecurityStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SecurityViewModel @Inject constructor() :
    BaseViewModel(),
    UiOwner<SecurityStateListener, SecurityDataListener> {

    override val uiState = MutableStateFlow(SecurityStateListener())
    override val uiData = MutableStateFlow(SecurityDataListener())

    fun toggleBiometric(enabled: Boolean) {
        uiData.value = uiData.value.copy(biometricEnabled = enabled)
    }

    fun logoutAllDevice() {
        // TODO API logout all device
    }

    fun deleteAccount() {
        // TODO API delete account
    }
}