/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.SearchFood
import com.mtv.based.core.network.utils.LoadState

data class HomeUiState(
    val customer: LoadState<Customer> = LoadState.Idle,
    val foods: LoadState<List<SearchFood>> = LoadState.Idle,
    val favoriteIds: Set<String> = emptySet(),
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isLastPage: Boolean = false,
    val page: Int = 0
)

sealed class HomeEvent {
    object Load : HomeEvent()
    object DismissDialog : HomeEvent()
    object LoadNextPage : HomeEvent()
    object ClickSearch : HomeEvent()
    data class ToggleFavorite(val foodId: String) : HomeEvent()
    data class ClickCategory(val value: String) : HomeEvent()
    object ClickNotif : HomeEvent()
    data class ClickFood(val id: String) : HomeEvent()
}

sealed class HomeEffect {
    data class NavigateToSearch(val query: String = "") : HomeEffect()
    object NavigateToNotif : HomeEffect()
    data class NavigateToDetail(val id: String) : HomeEffect()
}
