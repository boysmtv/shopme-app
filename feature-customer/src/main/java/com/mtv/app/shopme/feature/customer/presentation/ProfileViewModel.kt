/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.46
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.usecase.CustomerUseCase
import com.mtv.app.shopme.feature.customer.contract.ProfileEffect
import com.mtv.app.shopme.feature.customer.contract.ProfileEvent
import com.mtv.app.shopme.feature.customer.contract.ProfileUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val securePrefs: SecurePrefs,
    private val customerUseCase: CustomerUseCase,
) : BaseEventViewModel<ProfileEvent, ProfileEffect>() {

    private val _state = MutableStateFlow(ProfileUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.Load -> load()
            is ProfileEvent.DismissDialog -> dismissDialog()

            is ProfileEvent.ClickEditProfile -> emitEffect(ProfileEffect.NavigateToEditProfile)
            is ProfileEvent.ClickOrderHistory -> emitEffect(ProfileEffect.NavigateToOrderHistory)
            is ProfileEvent.ClickSettings -> emitEffect(ProfileEffect.NavigateToSettings)
            is ProfileEvent.ClickHelpCenter -> emitEffect(ProfileEffect.NavigateToHelpCenter)
            is ProfileEvent.ClickOrder -> emitEffect(ProfileEffect.NavigateToOrder)

            is ProfileEvent.ClickCheckTncCafe -> handleCheckTnc()
            is ProfileEvent.ClickLogout -> logout()
        }
    }

    private fun load() {
        observeCustomer()
    }

    private fun observeCustomer() {
        observeDataFlow(
            flow = customerUseCase(),
            onState = { state ->
                _state.update {
                    it.copy(customer = state)
                }
            },
            onError = {
                showError(it)
            }
        )
    }

    private fun handleCheckTnc() {
        if (doCheckTncCafe()) {
            emitEffect(ProfileEffect.NavigateToTnc)
        } else {
            emitEffect(ProfileEffect.NavigateToSeller)
        }
    }

    private fun logout() {
        securePrefs.clear()
        emitEffect(ProfileEffect.NavigateToLogin)
    }

    private fun doCheckTncCafe(): Boolean {
        return false
    }

    private fun showError(error: UiError) {
        setDialog(
            UiDialog.Center(
                state = DialogStateV1(
                    type = DialogType.ERROR,
                    title = ErrorMessages.GENERIC_ERROR,
                    message = error.message
                ),
                onPrimary = {
                    dismissDialog()
                }
            )
        )
    }
}
