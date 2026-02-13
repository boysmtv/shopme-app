/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: EditAddressViewModel.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.04
 */

package com.mtv.app.shopme.feature.presentation

import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import androidx.lifecycle.viewModelScope
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.shopme.feature.contract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditAddressViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val securePrefs: SecurePrefs
) : BaseViewModel(), UiOwner<EditAddressStateListener, EditAddressDataListener> {

    override val uiState = MutableStateFlow(EditAddressStateListener())

    override val uiData = MutableStateFlow(EditAddressDataListener())

    fun onSaveProfile(name: String, phone: String, address: String) {
        viewModelScope.launch {
            securePrefs.putString("name", name)
            securePrefs.putString("phone", phone)
            securePrefs.putString("address", address)

            uiData.value = EditAddressDataListener(name, phone, address)
            uiState.value = EditAddressStateListener()
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            val name = securePrefs.getString("name") ?: "Boy"
            val phone = securePrefs.getString("phone") ?: "08158844424"
            val address = securePrefs.getString("address") ?: "Puri Lestari - Blok G06/01"
            uiData.value = EditAddressDataListener(name, phone, address)
        }
    }
}
