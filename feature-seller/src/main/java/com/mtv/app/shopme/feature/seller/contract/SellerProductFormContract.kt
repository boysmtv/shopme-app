/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductFormContract.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 13.42
 */

package com.mtv.app.shopme.feature.seller.contract

import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

class SellerProductFormStateListener(
    var isSaving: Boolean = false
)

class SellerProductFormDataListener(
    var selectedProduct: ProductItem? = null
)

class SellerProductFormEventListener(
    val onSaveProduct: (ProductItem) -> Unit,
    val onDeleteProduct: (ProductItem) -> Unit
)

class SellerProductFormNavigationListener(
    val onBack: () -> Unit
)

data class ProductItem(
    val id: String = EMPTY_STRING,
    val name: String = EMPTY_STRING,
    val price: String = EMPTY_STRING,
    val stock: Int = 0,
    val category: String = EMPTY_STRING,
    val description: String = EMPTY_STRING
)