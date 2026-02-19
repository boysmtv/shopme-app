/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductListViewModel.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.58
 */

package com.mtv.app.shopme.feature.seller.presentation
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.core.provider.utils.SessionManager
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.seller.contract.ProductItem
import com.mtv.app.shopme.feature.seller.contract.SellerProductListDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerProductListStateListener
import com.mtv.app.shopme.feature.seller.model.SellerProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SellerProductListViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : BaseViewModel(),
    UiOwner<SellerProductListStateListener, SellerProductListDataListener> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(SellerProductListStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(SellerProductListDataListener())

    init {
        loadProducts()
    }

    private fun loadProducts() {

    }

    fun deleteProduct(product: ProductItem) {

    }

    private fun dummyProducts(): List<ProductItem> {
        return listOf(
            ProductItem("1", "Coffee Latte", "25000", 10, "Drink", "Hot coffee"),
            ProductItem("2", "Croissant", "15000", 20, "Food", "Butter croissant")
        )
    }
}

fun mockSellerProducts(): List<SellerProduct> = listOf(
    SellerProduct("1", "Double Beef Burger", "Rp 60.000", 10),
    SellerProduct("2", "Cheese Pizza", "Rp 75.000", 5),
    SellerProduct("3", "Padang Rice Set", "Rp 50.000", 12)
)
