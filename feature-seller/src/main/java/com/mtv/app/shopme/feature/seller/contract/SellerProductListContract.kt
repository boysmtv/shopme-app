/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductListContract.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.53
 */

package com.mtv.app.shopme.feature.seller.contract

class SellerProductListStateListener(
    var isLoading: Boolean = false
)

class SellerProductListDataListener(
    var productList: List<ProductItem> = emptyList()
)

class SellerProductListEventListener(
    val onDeleteProduct: (ProductItem) -> Unit
)

class SellerProductListNavigationListener(
    val onBack: () -> Unit,
    val onNavigateToAdd: () -> Unit,
    val onNavigateToEdit: (ProductItem) -> Unit
)
