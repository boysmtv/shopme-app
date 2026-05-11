/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProfileViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 10.00
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.usecase.GetSellerProfileUseCase
import com.mtv.app.shopme.domain.usecase.UpdateSellerAvailabilityUseCase
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
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
class SellerProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val getSellerProfileUseCase: GetSellerProfileUseCase,
    private val updateSellerAvailabilityUseCase: UpdateSellerAvailabilityUseCase
) : BaseEventViewModel<SellerStoreEvent, SellerStoreEffect>() {

    private val _state = MutableStateFlow(SellerStoreUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerStoreEvent) {
        when (event) {

            SellerStoreEvent.Load -> load()

            SellerStoreEvent.DismissDialog -> dismissDialog()

            SellerStoreEvent.ToggleOnline ->
                toggleOnline()

            SellerStoreEvent.ClickOrderHistory ->
                emitEffect(SellerStoreEffect.NavigateToOrders)

            SellerStoreEvent.ClickEditProfile ->
                emitEffect(SellerStoreEffect.NavigateToEditProfile)

            SellerStoreEvent.ClickStoreSettings ->
                emitEffect(SellerStoreEffect.NavigateToStoreSettings)

            SellerStoreEvent.ClickBankAccount ->
                emitEffect(SellerStoreEffect.NavigateToBankAccount)

            SellerStoreEvent.ClickChangePassword ->
                emitEffect(SellerStoreEffect.NavigateToChangePassword)

            SellerStoreEvent.ClickHelpCenter ->
                emitEffect(SellerStoreEffect.NavigateToHelpCenter)

            SellerStoreEvent.ClickBackToCustomer ->
                emitEffect(SellerStoreEffect.NavigateToCustomerHome)

            SellerStoreEvent.ClickBack -> emitEffect(SellerStoreEffect.NavigateBack)
            SellerStoreEvent.Logout -> logout()
            else -> {}
        }
    }

    private fun update(block: SellerStoreUiState.() -> SellerStoreUiState) {
        _state.update { it.block() }
    }

    private fun load() {
        observeDataFlow(
            flow = getSellerProfileUseCase(),
            onState = { result ->
                _state.update {
                    val profile = (result as? LoadState.Success)?.data
                    it.copy(
                        isLoading = result is LoadState.Loading,
                        sellerName = profile?.sellerName ?: it.sellerName,
                        sellerPhoto = profile?.sellerPhoto ?: it.sellerPhoto,
                        email = profile?.email ?: it.email,
                        phone = profile?.phone ?: it.phone,
                        storeName = profile?.storeName ?: it.storeName,
                        storePhoto = profile?.storePhoto ?: it.storePhoto,
                        storeAddress = profile?.storeAddress ?: it.storeAddress,
                        isOnline = profile?.isOnline ?: it.isOnline
                    )
                }
            },
            onError = ::showError
        )
    }

    private fun toggleOnline() {
        val target = !_state.value.isOnline
        observeDataFlow(
            flow = updateSellerAvailabilityUseCase(target),
            onState = { result ->
                _state.update {
                    val profile = (result as? LoadState.Success)?.data
                    it.copy(
                        isLoading = result is LoadState.Loading,
                        sellerName = profile?.sellerName ?: it.sellerName,
                        sellerPhoto = profile?.sellerPhoto ?: it.sellerPhoto,
                        email = profile?.email ?: it.email,
                        phone = profile?.phone ?: it.phone,
                        storeName = profile?.storeName ?: it.storeName,
                        storePhoto = profile?.storePhoto ?: it.storePhoto,
                        storeAddress = profile?.storeAddress ?: it.storeAddress,
                        isOnline = profile?.isOnline ?: it.isOnline
                    )
                }
            },
            onError = ::showError
        )
    }

    private fun logout() {
        sessionManager.logout()
        emitEffect(SellerStoreEffect.LogoutSuccess)
    }

    private fun showError(error: UiError) {
        handleSessionError(
            error = error,
            sessionManager = sessionManager,
            beforeLogout = { _state.update { it.copy(isLoading = false) } }
        ) {
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
