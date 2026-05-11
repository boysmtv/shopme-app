/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductListContract.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.53
 */

package com.mtv.app.shopme.feature.seller.contract

import com.mtv.app.shopme.domain.model.ProductItem

data class SellerProductListUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val products: List<ProductItem> = emptyList()
)

sealed class SellerProductListEvent {
    object Load : SellerProductListEvent()
    object DismissDialog : SellerProductListEvent()

    object ClickAdd : SellerProductListEvent()
    data class ClickEdit(val productId: String) : SellerProductListEvent()
    data class ClickDelete(val productId: String) : SellerProductListEvent()

    object ClickBack : SellerProductListEvent()
}

sealed class SellerProductListEffect {
    object NavigateBack : SellerProductListEffect()
    object NavigateToAdd : SellerProductListEffect()
    data class NavigateToEdit(val productId: String) : SellerProductListEffect()
}
