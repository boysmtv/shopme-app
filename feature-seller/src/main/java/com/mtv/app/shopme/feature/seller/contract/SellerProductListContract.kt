/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductListContract.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 08.53
 */

package com.mtv.app.shopme.feature.seller.contract

import androidx.compose.runtime.Immutable
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.model.ProductItem
import com.mtv.app.shopme.domain.model.ProductStats

@Immutable
data class SellerProductListUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val currentPage: Int = 0,
    val isLastPage: Boolean = false,
    val errorMessage: String? = null,
    val cafeId: String? = null,
    val searchQuery: String = "",
    val categoryFilter: FoodCategory? = null,
    val statusFilter: FoodStatus? = null,
    val activeFilter: Boolean? = null,
    val productStats: ProductStats? = null,
    val isSelectionMode: Boolean = false,
    val selectedProductIds: Set<String> = emptySet(),

    val products: List<ProductItem> = emptyList()
)

sealed class SellerProductListEvent {
    object Load : SellerProductListEvent()
    object LoadMore : SellerProductListEvent()
    object DismissDialog : SellerProductListEvent()

    object ClickAdd : SellerProductListEvent()
    data class ClickEdit(val productId: String) : SellerProductListEvent()
    data class ClickDelete(val productId: String) : SellerProductListEvent()
    data class ChangeSearchQuery(val value: String) : SellerProductListEvent()
    data class SelectCategoryFilter(val value: FoodCategory?) : SellerProductListEvent()
    data class SelectStatusFilter(val value: FoodStatus?) : SellerProductListEvent()
    data class SelectActiveFilter(val value: Boolean?) : SellerProductListEvent()
    object ClearFilters : SellerProductListEvent()

    object ClickBack : SellerProductListEvent()

    object ToggleSelectionMode : SellerProductListEvent()
    data class ToggleProductSelection(val productId: String) : SellerProductListEvent()
    object SelectAll : SellerProductListEvent()
    object DeselectAll : SellerProductListEvent()
    object BulkActivate : SellerProductListEvent()
    object BulkDeactivate : SellerProductListEvent()
    object BulkDelete : SellerProductListEvent()
}

sealed class SellerProductListEffect {
    object NavigateBack : SellerProductListEffect()
    object NavigateToAdd : SellerProductListEffect()
    data class NavigateToEdit(val productId: String) : SellerProductListEffect()
}
