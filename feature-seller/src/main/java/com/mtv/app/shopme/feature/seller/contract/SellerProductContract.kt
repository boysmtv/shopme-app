/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductContract.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.53
 */

package com.mtv.app.shopme.feature.seller.contract

import com.mtv.app.shopme.feature.seller.model.SellerProduct

data class SellerProductStateListener(
    val productList: List<SellerProduct> = emptyList()
)

data class SellerProductDataListener(
    val selectedProduct: SellerProduct? = null
)

data class SellerProductEventListener(
    val onAddProduct: (SellerProduct) -> Unit = {},
    val onUpdateProduct: (SellerProduct) -> Unit = {},
    val onDeleteProduct: (String) -> Unit = {}
)

data class SellerProductNavigationListener(
    val onBack: () -> Unit = {},
    val navigateToAddProduct: () -> Unit = {},
    val navigateToEditProduct: (SellerProduct) -> Unit = {}
)
