/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: EditProfileViewModel.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.04
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.customer.contract.EditProfileDataListener
import com.mtv.app.shopme.feature.customer.contract.EditProfileStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val securePrefs: SecurePrefs
) : BaseViewModel(), UiOwner<EditProfileStateListener, EditProfileDataListener> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(EditProfileStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(EditProfileDataListener())

    fun onSaveProfile(name: String, phone: String, address: String) {
        viewModelScope.launch {
            securePrefs.putString("name", name)
            securePrefs.putString("phone", phone)
            securePrefs.putString("address", address)

            uiData.value = EditProfileDataListener(name, phone, address)
            uiState.value = EditProfileStateListener()
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            val name = securePrefs.getString("name") ?: "Boy"
            val phone = securePrefs.getString("phone") ?: "08158844424"
            val address = securePrefs.getString("address") ?: "Puri Lestari - Blok G06/01"
            uiData.value = EditProfileDataListener(name, phone, address)
        }
    }
}
