/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductViewModel.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.58
 */

package com.mtv.app.shopme.feature.seller.presentation
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.seller.contract.SellerProductDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerProductStateListener
import com.mtv.app.shopme.feature.seller.model.SellerProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SellerProductViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : BaseViewModel(),
    UiOwner<SellerProductStateListener, SellerProductDataListener> {

    override val uiState = MutableStateFlow(
        SellerProductStateListener(productList = mockSellerProducts())
    )
    override val uiData = MutableStateFlow(SellerProductDataListener())

    fun addProduct(product: SellerProduct) {
        uiState.value = uiState.value.copy(productList = uiState.value.productList + product)
    }

    fun updateProduct(product: SellerProduct) {
        val updated = uiState.value.productList.map {
            if (it.id == product.id) product else it
        }
        uiState.value = uiState.value.copy(productList = updated)
    }

    fun deleteProduct(productId: String) {
        uiState.value = uiState.value.copy(
            productList = uiState.value.productList.filter { it.id != productId }
        )
    }
}

fun mockSellerProducts(): List<SellerProduct> = listOf(
    SellerProduct("1", "Double Beef Burger", "Rp 60.000", 10),
    SellerProduct("2", "Cheese Pizza", "Rp 75.000", 5),
    SellerProduct("3", "Padang Rice Set", "Rp 50.000", 12)
)
