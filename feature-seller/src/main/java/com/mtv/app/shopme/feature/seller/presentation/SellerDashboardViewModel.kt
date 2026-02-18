/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerDashboardViewModel.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 12.15
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.seller.contract.SellerDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SellerDashboardViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : BaseViewModel(),
    UiOwner<SellerStateListener, SellerDataListener> {

    override val uiState = MutableStateFlow(SellerStateListener())
    override val uiData = MutableStateFlow(SellerDataListener())
}
