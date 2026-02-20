/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProfileViewModel.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 10.00
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.seller.contract.SellerProfileDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerProfileStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SellerProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : BaseViewModel(),
    UiOwner<SellerProfileStateListener, SellerProfileDataListener> {

    override val uiState = MutableStateFlow(SellerProfileStateListener())

    override val uiData = MutableStateFlow(
        SellerProfileDataListener(
            sellerName = "Dedy Wijaya",
            email = "seller@email.com",
            phone = "08123456789",
            storeName = "Shopme Store",
            storeAddress = "Jakarta, Indonesia",
            isOnline = true
        )
    )

    fun toggleOnline() {

    }

    fun logout() {
        sessionManager.logout()
    }
}

