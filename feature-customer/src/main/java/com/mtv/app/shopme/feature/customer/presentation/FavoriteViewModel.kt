package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.usecase.AddFavoriteFoodUseCase
import com.mtv.app.shopme.domain.usecase.GetFavoriteFoodIdsUseCase
import com.mtv.app.shopme.domain.usecase.GetFavoriteFoodsUseCase
import com.mtv.app.shopme.domain.usecase.RemoveFavoriteFoodUseCase
import com.mtv.app.shopme.feature.customer.contract.FavoriteEffect
import com.mtv.app.shopme.feature.customer.contract.FavoriteEvent
import com.mtv.app.shopme.feature.customer.contract.FavoriteUiState
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SessionManager
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteFoodsUseCase: GetFavoriteFoodsUseCase,
    private val getFavoriteFoodIdsUseCase: GetFavoriteFoodIdsUseCase,
    private val addFavoriteFoodUseCase: AddFavoriteFoodUseCase,
    private val removeFavoriteFoodUseCase: RemoveFavoriteFoodUseCase,
    private val sessionManager: SessionManager
) : BaseEventViewModel<FavoriteEvent, FavoriteEffect>() {

    private val _state = MutableStateFlow(FavoriteUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: FavoriteEvent) {
        when (event) {
            FavoriteEvent.Load -> load()
            FavoriteEvent.DismissDialog -> dismissDialog()
            FavoriteEvent.ClickBack -> emitEffect(FavoriteEffect.NavigateBack)
            is FavoriteEvent.ClickFood -> emitEffect(FavoriteEffect.NavigateToDetail(event.foodId))
            is FavoriteEvent.ToggleFavorite -> toggleFavorite(event.foodId)
        }
    }

    private fun load() {
        observeFavorites()
        observeFavoriteItems()
    }

    private fun observeFavoriteItems() {
        observeIndependentDataFlow(
            flow = getFavoriteFoodsUseCase(),
            onState = { state -> _state.update { it.copy(foods = state) } },
            onError = ::showError
        )
    }

    private fun observeFavorites() {
        observeIndependentDataFlow(
            flow = getFavoriteFoodIdsUseCase(),
            onSuccess = { ids -> _state.update { it.copy(favoriteIds = ids.toSet()) } },
            onError = ::showError
        )
    }

    private fun toggleFavorite(foodId: String) {
        val isFavorite = _state.value.favoriteIds.contains(foodId)
        val flow = if (isFavorite) {
            removeFavoriteFoodUseCase(foodId)
        } else {
            addFavoriteFoodUseCase(foodId)
        }

        observeDataFlow(
            flow = flow,
            onSuccess = {
                _state.update { current ->
                    val updatedIds = current.favoriteIds.toMutableSet().apply {
                        if (isFavorite) remove(foodId) else add(foodId)
                    }
                    val updatedFoods = when {
                        !isFavorite -> current.foods
                        current.foods is LoadState.Success -> LoadState.Success(
                            current.foods.data.filterNot { it.id == foodId }
                        )
                        else -> current.foods
                    }
                    current.copy(
                        favoriteIds = updatedIds,
                        foods = updatedFoods
                    )
                }
            },
            onError = ::showError
        )
    }

    private fun showError(error: UiError) {
        handleSessionError(error, sessionManager) {
            setDialog(
                UiDialog.Center(
                    state = DialogStateV1(
                        type = DialogType.ERROR,
                        title = ErrorMessages.GENERIC_ERROR,
                        message = it.message
                    ),
                    onPrimary = { dismissDialog() }
                )
            )
        }
    }
}
