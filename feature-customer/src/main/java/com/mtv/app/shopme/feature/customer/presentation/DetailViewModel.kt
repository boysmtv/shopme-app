/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DetailViewModel.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 09.01
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.SavedStateHandle
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.data.remote.request.FoodAddToCartRequest
import com.mtv.app.shopme.domain.usecase.FoodAddToCartUseCase
import com.mtv.app.shopme.domain.usecase.FoodDetailUseCase
import com.mtv.app.shopme.domain.usecase.FoodSimilarUseCase
import com.mtv.app.shopme.feature.customer.contract.DetailEffect
import com.mtv.app.shopme.feature.customer.contract.DetailEvent
import com.mtv.app.shopme.feature.customer.contract.DetailUiState
import com.mtv.based.core.network.utils.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val foodDetailUseCase: FoodDetailUseCase,
    private val foodSimilarUseCase: FoodSimilarUseCase,
    private val foodAddToCartUseCase: FoodAddToCartUseCase,
    savedStateHandle: SavedStateHandle
) : BaseEventViewModel<DetailEvent, DetailEffect>() {

    private val _state = MutableStateFlow(DetailUiState())
    val uiState = _state.asStateFlow()

    private val foodId: String = checkNotNull(savedStateHandle["foodId"])

    init {
        loadDetail()
    }

    override fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.BackClicked -> emitEffect(DetailEffect.NavigateBack)
            is DetailEvent.ChatClicked -> emitEffect(DetailEffect.NavigateToChat)
            is DetailEvent.OpenCart -> emitEffect(DetailEffect.NavigateToCart)
            is DetailEvent.ClickCafe -> emitEffect(DetailEffect.NavigateToCafe(event.cafeId))
            is DetailEvent.ClickSimilarFood -> emitEffect(DetailEffect.NavigateToDetail(event.foodId))
            is DetailEvent.AddToCart -> doAddToCart(event)
        }
    }

    private fun loadDetail() {
        observeDataFlow(
            flow = foodDetailUseCase(foodId),
            onState = { state ->
                when (state) {
                    is LoadState.Loading -> _state.update { it.copy(food = LoadState.Loading) }
                    is LoadState.Success -> {
                        val food = state.data.data
                        _state.update { it.copy(food = LoadState.Success(food!!)) }
                        food?.cafeId?.let { loadSimilar(it) }
                    }
                    is LoadState.Error -> _state.update { it.copy(food = LoadState.Error(state.error)) }
                    else -> Unit
                }
            }
        )
    }

    private fun loadSimilar(cafeId: String) {
        observeDataFlow(
            flow = foodSimilarUseCase(cafeId),
            onState = { state ->
                when (state) {
                    is LoadState.Loading -> _state.update { it.copy(similarFoods = LoadState.Loading) }
                    is LoadState.Success -> {
                        val list = state.data.data.orEmpty()
                        _state.update { it.copy(similarFoods = LoadState.Success(list)) }
                    }
                    is LoadState.Error -> _state.update { it.copy(similarFoods = LoadState.Error(state.error)) }
                    else -> Unit
                }
            }
        )
    }

    private fun doAddToCart(event: DetailEvent.AddToCart) {
        observeDataFlow(
            flow = foodAddToCartUseCase(
                FoodAddToCartRequest(
                    foodId = event.foodId,
                    variants = event.variants,
                    quantity = event.quantity,
                    note = event.note
                )
            ),
            onState = { state ->
                when (state) {
                    is LoadState.Loading -> _state.update { it.copy(addToCartState = LoadState.Loading) }
                    is LoadState.Success -> {
                        _state.update { it.copy(addToCartState = LoadState.Success(Unit)) }
                        emitEffect(DetailEffect.NavigateToCart)
                    }
                    is LoadState.Error -> _state.update { it.copy(addToCartState = LoadState.Error(state.error)) }
                    else -> Unit
                }
            }
        )
    }
}