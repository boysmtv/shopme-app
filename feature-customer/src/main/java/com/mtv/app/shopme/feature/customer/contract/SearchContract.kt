/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.remote.response.PageResponse
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.SearchFood
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class SearchUiState(
    val query: String = "",
    val foods: LoadState<List<SearchFood>> = LoadState.Idle,
    val favoriteIds: Set<String> = emptySet(),
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isLastPage: Boolean = false,
    val page: Int = 0
)

sealed class SearchEvent {
    object Load : SearchEvent()
    object LoadNextPage : SearchEvent()
    object BackClicked : SearchEvent()
    object DismissDialog : SearchEvent()
    object ClickFavorites : SearchEvent()
    data class ToggleFavorite(val foodId: String) : SearchEvent()

    data class QueryChanged(val query: String) : SearchEvent()
    data class ClickFood(val id: String) : SearchEvent()
}

sealed class SearchEffect {
    object NavigateBack : SearchEffect()
    object NavigateToFavorites : SearchEffect()
    data class NavigateToDetail(val id: String) : SearchEffect()
}
