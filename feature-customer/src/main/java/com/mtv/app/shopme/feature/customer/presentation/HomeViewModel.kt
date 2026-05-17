/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.45
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.param.SearchParam
import com.mtv.app.shopme.domain.usecase.AddFavoriteFoodUseCase
import com.mtv.app.shopme.domain.usecase.GetCustomerUseCase
import com.mtv.app.shopme.domain.usecase.GetFavoriteFoodIdsUseCase
import com.mtv.app.shopme.domain.usecase.RemoveFavoriteFoodUseCase
import com.mtv.app.shopme.domain.usecase.GetSearchFoodUseCase
import com.mtv.app.shopme.feature.customer.contract.HomeEffect
import com.mtv.app.shopme.feature.customer.contract.HomeEvent
import com.mtv.app.shopme.feature.customer.contract.HomeUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
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
class HomeViewModel @Inject constructor(
    private val customerUseCase: GetCustomerUseCase,
    private val homeFoodUseCase: GetSearchFoodUseCase,
    private val getFavoriteFoodIdsUseCase: GetFavoriteFoodIdsUseCase,
    private val addFavoriteFoodUseCase: AddFavoriteFoodUseCase,
    private val removeFavoriteFoodUseCase: RemoveFavoriteFoodUseCase,
    private val sessionManager: SessionManager,
) : BaseEventViewModel<HomeEvent, HomeEffect>() {

    private val _state = MutableStateFlow(HomeUiState())
    val uiState = _state.asStateFlow()
    private var foodSeed: String = newFoodSeed()

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Load -> load()
            is HomeEvent.DismissDialog -> dismissDialog()
            HomeEvent.LoadNextPage -> loadNextPage()
            is HomeEvent.ToggleFavorite -> toggleFavorite(event.foodId)
            is HomeEvent.ClickFood -> emitEffect(HomeEffect.NavigateToDetail(event.id))
            is HomeEvent.ClickSearch -> emitEffect(HomeEffect.NavigateToSearch())
            is HomeEvent.ClickCategory -> emitEffect(HomeEffect.NavigateToSearch(event.value))
            is HomeEvent.ClickNotif -> emitEffect(HomeEffect.NavigateToNotif)
        }
    }

    fun load() {
        foodSeed = newFoodSeed()
        observeCustomer()
        observeFoods()
        observeFavorites()
    }

    private fun observeFavorites() {
        observeIndependentDataFlow(
            flow = getFavoriteFoodIdsUseCase(),
            onSuccess = { ids ->
                _state.update { it.copy(favoriteIds = ids.toSet()) }
            },
            onError = { showError(it) }
        )
    }

    private fun observeFoods() {
        observeIndependentDataFlow(
            flow = homeFoodUseCase(SearchParam(name = "", page = 0, sort = "random", seed = foodSeed)),
            onState = { state ->
                when (state) {
                    is LoadState.Loading -> _state.update {
                        if (it.foods is LoadState.Success) {
                            it.copy(isRefreshing = true, isLoadingMore = false)
                        } else {
                            it.copy(foods = LoadState.Loading, isRefreshing = false, isLoadingMore = false)
                        }
                    }
                    is LoadState.Success -> _state.update {
                        it.copy(
                            foods = LoadState.Success(state.data.content),
                            page = state.data.page,
                            isLastPage = state.data.last,
                            isRefreshing = false,
                            isLoadingMore = false
                        )
                    }
                    is LoadState.Error -> _state.update {
                        if (it.foods is LoadState.Success) {
                            it.copy(isRefreshing = false, isLoadingMore = false)
                        } else {
                            it.copy(foods = LoadState.Error(state.error), isRefreshing = false, isLoadingMore = false)
                        }
                    }
                    else -> Unit
                }
            },
            onError = {
                showError(it)
            }
        )
    }

    private fun loadNextPage() {
        val state = _state.value
        if (state.isLastPage || state.isLoadingMore || state.foods is LoadState.Loading) return
        observeIndependentDataFlow(
            flow = homeFoodUseCase(SearchParam(name = "", page = state.page + 1, sort = "random", seed = foodSeed)),
            onState = { result ->
                when (result) {
                    is LoadState.Loading -> _state.update { it.copy(isLoadingMore = true) }
                    is LoadState.Success -> {
                        val current = (state.foods as? LoadState.Success)?.data.orEmpty()
                        _state.update {
                            it.copy(
                                foods = LoadState.Success(current + result.data.content),
                                page = result.data.page,
                                isLastPage = result.data.last,
                                isLoadingMore = false
                            )
                        }
                    }
                    is LoadState.Error -> _state.update { it.copy(isLoadingMore = false) }
                    else -> Unit
                }
            },
            onError = { showError(it) }
        )
    }

    private fun observeCustomer() {
        observeIndependentDataFlow(
            flow = customerUseCase(),
            onState = { state ->
                _state.update {
                    if (state is LoadState.Loading && it.customer is LoadState.Success) {
                        it.copy(isRefreshing = true)
                    } else {
                        it.copy(customer = state, isRefreshing = state is LoadState.Loading && it.foods is LoadState.Success)
                    }
                }
            },
            onError = {
                showError(it)
            }
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
                    val updated = current.favoriteIds.toMutableSet().apply {
                        if (isFavorite) remove(foodId) else add(foodId)
                    }
                    current.copy(favoriteIds = updated)
                }
            },
            onError = { showError(it) }
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
                    onPrimary = {
                        dismissDialog()
                    }
                )
            )
        }
    }

    private fun newFoodSeed(): String = System.currentTimeMillis().toString()

}
