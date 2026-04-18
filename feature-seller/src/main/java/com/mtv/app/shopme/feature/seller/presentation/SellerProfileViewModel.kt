/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProfileViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 10.00
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.based.core.provider.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SellerProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : BaseEventViewModel<SellerStoreEvent, SellerStoreEffect>() {

    private val _state = MutableStateFlow(
        SellerStoreUiState(
            sellerName = "Dedy Wijaya",
            email = "seller@email.com",
            phone = "08123456789",
            storeName = "Shopme Store",
            storeAddress = "Jakarta, Indonesia",
            isOnline = true
        )
    )
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SellerStoreEvent) {
        when (event) {

            SellerStoreEvent.Load -> {}

            SellerStoreEvent.DismissDialog -> dismissDialog()

            SellerStoreEvent.ToggleOnline ->
                update { copy(isOnline = !isOnline) }

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

            SellerStoreEvent.ClickBack -> TODO()
            SellerStoreEvent.Logout -> logout()
            else -> {}
        }
    }

    private fun update(block: SellerStoreUiState.() -> SellerStoreUiState) {
        _state.update { it.block() }
    }

    private fun logout() {
        sessionManager.logout()
        emitEffect(SellerStoreEffect.LogoutSuccess)
    }
}
