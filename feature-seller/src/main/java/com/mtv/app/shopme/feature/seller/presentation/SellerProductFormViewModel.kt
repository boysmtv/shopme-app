/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductFormViewModel.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 13.46
 */

package com.mtv.app.shopme.feature.seller.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.seller.contract.ProductItem
import com.mtv.app.shopme.feature.seller.contract.SellerProductFormDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerProductFormStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SellerProductFormViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : BaseViewModel(),
    UiOwner<SellerProductFormStateListener, SellerProductFormDataListener> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(SellerProductFormStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(SellerProductFormDataListener())

    fun loadProduct(productId: String) {

    }

    fun saveProduct(product: ProductItem) {
        // Call repository save
    }

    fun deleteProduct(product: ProductItem) {
        // Call repository delete
    }
}
