/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AccountSettingsViewModel.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 22.59
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.customer.contract.SettingsDataListener
import com.mtv.app.shopme.feature.customer.contract.SettingsStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : BaseViewModel(),
    UiOwner<SettingsStateListener, SettingsDataListener> {

    override val uiState = MutableStateFlow(SettingsStateListener())

    override val uiData = MutableStateFlow(SettingsDataListener())

    fun toggleNotification(enabled: Boolean) {
        uiData.value = uiData.value.copy(notificationEnabled = enabled)
    }

    fun toggleDarkMode(enabled: Boolean) {
        uiData.value = uiData.value.copy(darkMode = enabled)
    }

    fun logout() {
        sessionManager.logout()
    }
}