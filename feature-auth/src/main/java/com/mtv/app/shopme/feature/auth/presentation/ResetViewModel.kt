/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ResetViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.54
 */

package com.mtv.app.shopme.feature.auth.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.auth.contract.ResetDataListener
import com.mtv.app.shopme.feature.auth.contract.ResetStateListener
import com.mtv.based.core.network.utils.ResourceFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetViewModel @Inject constructor() :
    BaseViewModel(),
    UiOwner<ResetStateListener, ResetDataListener> {

    override val uiState = MutableStateFlow(ResetStateListener())
    override val uiData = MutableStateFlow(ResetDataListener())

    fun updateEmail(value: String) {
        uiData.value = uiData.value.copy(email = value)
    }

    fun resetPassword() {
        viewModelScope.launch {
            uiState.value =
                uiState.value.copy(resetState = ResourceFirebase.Loading)

            delay(800)

            uiState.value =
                uiState.value.copy(resetState = ResourceFirebase.Success("Reset Link Sent"))
        }
    }
}