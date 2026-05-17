/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.46
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.usecase.GetCustomerUseCase
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.feature.customer.contract.ProfileEffect
import com.mtv.app.shopme.feature.customer.contract.ProfileEvent
import com.mtv.app.shopme.feature.customer.contract.ProfileUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.based.core.provider.utils.SessionManager
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
    private val sessionManager: SessionManager,
    private val customerUseCase: GetCustomerUseCase,
    private val getSellerProfileUseCase: GetSellerProfileUseCase,
) : BaseEventViewModel<ProfileEvent, ProfileEffect>() {

    private val _state = MutableStateFlow(ProfileUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.Load -> load()
            is ProfileEvent.DismissDialog -> dismissDialog()
            is ProfileEvent.ClickEditProfile -> emitEffect(ProfileEffect.NavigateToEditProfile)
            is ProfileEvent.ClickOrderHistory -> emitEffect(ProfileEffect.NavigateToOrderHistory)
            is ProfileEvent.ClickFavorites -> emitEffect(ProfileEffect.NavigateToFavorites)
            is ProfileEvent.ClickSettings -> emitEffect(ProfileEffect.NavigateToSettings)
            is ProfileEvent.ClickHelpCenter -> emitEffect(ProfileEffect.NavigateToHelpCenter)
            is ProfileEvent.ClickOrder -> emitEffect(ProfileEffect.NavigateToOrder(event.filter))
            is ProfileEvent.ClickCheckTncCafe -> handleCheckTnc()
            is ProfileEvent.ClickLogout -> logout()
        }
    }

    private fun load() { observeCustomer() }

    private fun observeCustomer() {
        observeDataFlow(
            flow = customerUseCase(),
            onState = { state ->
                _state.update {
                    when (state) {
                        is LoadState.Loading -> {
                            if (it.customer is LoadState.Success) it.copy(isRefreshing = true)
                            else it.copy(customer = LoadState.Loading, isRefreshing = false)
                        }
                        is LoadState.Success -> it.copy(customer = state, isRefreshing = false)
                        is LoadState.Error -> {
                            if (it.customer is LoadState.Success) it.copy(isRefreshing = false)
                            else it.copy(customer = state, isRefreshing = false)
                        }
                        else -> it.copy(customer = state, isRefreshing = false)
                    }
                }
            },
            onError = {
                _state.update { it.copy(isRefreshing = false) }
                showError(it)
            }
        )
    }

    private fun handleCheckTnc() {
        observeDataFlow(
            flow = getSellerProfileUseCase(),
            onState = { state ->
                if (state is LoadState.Success) {
                    if (state.data.hasCafe) emitEffect(ProfileEffect.NavigateToSeller)
                    else emitEffect(ProfileEffect.NavigateToTnc)
                }
            },
            onError = { emitEffect(ProfileEffect.NavigateToTnc) }
        )
    }

    private fun logout() {
        securePrefs.clear()
        emitEffect(ProfileEffect.NavigateToLogin)
    }

    private fun showError(error: UiError) {
        handleSessionError(error, sessionManager) {
            setDialog(
                UiDialog.Center(
                    state = DialogStateV1(
                        type = DialogType.ERROR,
                        title = ErrorMessages.GENERIC_ERROR,
                        message = it.message
                    ),
                    onPrimary = { dismissDialog() }
                )
            )
        }
    }
}
