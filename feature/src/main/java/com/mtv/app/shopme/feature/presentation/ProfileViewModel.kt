/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.46
 */

package com.mtv.app.shopme.feature.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.contract.ProfileDataListener
import com.mtv.app.shopme.feature.contract.ProfileDialog
import com.mtv.app.shopme.feature.contract.ProfileStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val securePrefs: SecurePrefs
) : BaseViewModel(), UiOwner<ProfileStateListener, ProfileDataListener> {

    override val uiState = MutableStateFlow(ProfileStateListener())

    override val uiData = MutableStateFlow(ProfileDataListener())

    fun showLogoutDialog() {
        uiState.value = uiState.value.copy(activeDialog = ProfileDialog.LogoutConfirm)
    }

    fun dismissDialog() {
        uiState.value = uiState.value.copy(activeDialog = null)
    }
}
