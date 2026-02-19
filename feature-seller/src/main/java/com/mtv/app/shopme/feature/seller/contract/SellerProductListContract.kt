/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductListContract.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.53
 */

package com.mtv.app.shopme.feature.seller.contract

import com.mtv.app.shopme.feature.seller.model.SellerProduct

class SellerProductListStateListener(
    var isLoading: Boolean = false,
    val productList: List<SellerProduct> = emptyList()
)

class SellerProductListDataListener(
    var productList: List<ProductItem> = emptyList()
)

class SellerProductListEventListener(
    val onDeleteProduct: () -> Unit
)

class SellerProductListNavigationListener(
    val onBack: () -> Unit,
    val onNavigateToAdd: () -> Unit,
    val onNavigateToEdit: () -> Unit
)
