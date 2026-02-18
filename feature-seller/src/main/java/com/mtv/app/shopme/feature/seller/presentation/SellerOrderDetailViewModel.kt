/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderDetailViewModel.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 13.04
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.data.OrderStatus
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SellerOrderDetailViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : BaseViewModel(),
    UiOwner<SellerOrderDetailStateListener, SellerOrderDetailDataListener> {

    override val uiState = MutableStateFlow(SellerOrderDetailStateListener())

    override val uiData = MutableStateFlow(SellerOrderDetailDataListener())

    fun init(orderId: String) {
        uiState.update {
            it.copy(orderId = orderId)
        }

        // Dummy load (nanti bisa pakai usecase)
        uiData.update {
            it.copy(
                customerName = "Dedy Wijaya",
                total = "Rp 120.000"
            )
        }
    }

    fun changeStatus(status: OrderStatus) {
        uiState.update {
            it.copy(currentStatus = status)
        }
    }

    fun saveStatus() {
        // nanti panggil usecase update status
    }
}
