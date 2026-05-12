package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.domain.model.SearchFood
import com.mtv.based.core.network.utils.LoadState

data class FavoriteUiState(
    val foods: LoadState<List<SearchFood>> = LoadState.Idle,
    val favoriteIds: Set<String> = emptySet()
)

sealed class FavoriteEvent {
    data object Load : FavoriteEvent()
    data object DismissDialog : FavoriteEvent()
    data object ClickBack : FavoriteEvent()
    data class ClickFood(val foodId: String) : FavoriteEvent()
    data class ToggleFavorite(val foodId: String) : FavoriteEvent()
}

sealed class FavoriteEffect {
    data object NavigateBack : FavoriteEffect()
    data class NavigateToDetail(val foodId: String) : FavoriteEffect()
}
