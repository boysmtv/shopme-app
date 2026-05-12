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
import com.mtv.app.shopme.domain.param.CartAddParam
import com.mtv.app.shopme.domain.param.CartAddVariantParam
import com.mtv.app.shopme.domain.usecase.AddFavoriteFoodUseCase
import com.mtv.app.shopme.domain.usecase.EnsureChatConversationUseCase
import com.mtv.app.shopme.domain.usecase.CreateFoodToCartUseCase
import com.mtv.app.shopme.domain.usecase.GetFavoriteFoodIdsUseCase
import com.mtv.app.shopme.domain.usecase.GetFoodDetailUseCase
import com.mtv.app.shopme.domain.usecase.GetFoodSimilarUseCase
import com.mtv.app.shopme.domain.usecase.RemoveFavoriteFoodUseCase
import com.mtv.app.shopme.feature.customer.contract.DetailEffect
import com.mtv.app.shopme.feature.customer.contract.DetailEvent
import com.mtv.app.shopme.feature.customer.contract.DetailUiState
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
class DetailViewModel @Inject constructor(
    private val ensureChatConversationUseCase: EnsureChatConversationUseCase,
    private val foodDetailUseCase: GetFoodDetailUseCase,
    private val foodSimilarUseCase: GetFoodSimilarUseCase,
    private val foodAddToCartUseCase: CreateFoodToCartUseCase,
    private val getFavoriteFoodIdsUseCase: GetFavoriteFoodIdsUseCase,
    private val addFavoriteFoodUseCase: AddFavoriteFoodUseCase,
    private val removeFavoriteFoodUseCase: RemoveFavoriteFoodUseCase,
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
) : BaseEventViewModel<DetailEvent, DetailEffect>() {

    private val _state = MutableStateFlow(DetailUiState())
    val uiState = _state.asStateFlow()

    private val foodId: String = checkNotNull(savedStateHandle["foodId"])

    override fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.Load -> loadDetail()
            is DetailEvent.BackClicked -> emitEffect(DetailEffect.NavigateBack)
            is DetailEvent.DismissDialog -> dismissDialog()
            is DetailEvent.ChatClicked -> openChat()
            is DetailEvent.ToggleFavorite -> toggleFavorite()
            is DetailEvent.OpenCart -> emitEffect(DetailEffect.NavigateToCart)
            is DetailEvent.ClickCafe -> emitEffect(DetailEffect.NavigateToCafe(event.cafeId))
            is DetailEvent.ClickSimilarFood -> emitEffect(DetailEffect.NavigateToDetail(event.foodId))
            is DetailEvent.AddToCart -> doAddToCart(event)
        }
    }

    private fun openChat() {
        val cafeId = (_state.value.food as? LoadState.Success)?.data?.cafeId.orEmpty()
        if (cafeId.isBlank()) return

        observeDataFlow(
            flow = ensureChatConversationUseCase(cafeId),
            onSuccess = { emitEffect(DetailEffect.NavigateToChat(it)) }
        )
    }

    private fun loadDetail() {
        observeFavorites()
        observeDataFlow(
            flow = foodDetailUseCase(foodId),
            onState = { state ->
                _state.update { it.copy(food = state) }

                if (state is LoadState.Success) {
                    val food = state.data
                    loadSimilar(food.cafeId)
                }
            }
        )
    }

    private fun observeFavorites() {
        observeIndependentDataFlow(
            flow = getFavoriteFoodIdsUseCase(),
            onSuccess = { ids ->
                _state.update { it.copy(isFavorite = ids.contains(foodId)) }
            },
            onError = { showError(it) }
        )
    }

    private fun loadSimilar(cafeId: String) {
        observeDataFlow(
            flow = foodSimilarUseCase(cafeId),
            onState = { state ->
                _state.update { it.copy(similarFoods = state) }
            }
        )
    }

    private fun doAddToCart(event: DetailEvent.AddToCart) {
        val param = CartAddParam(
            foodId = event.foodId,
            quantity = event.quantity,
            note = event.note,
            variants = event.variants.map { cartVariant ->
                CartAddVariantParam(
                    variantId = cartVariant.variantId,
                    optionId = cartVariant.optionId
                )
            },
        )

        observeDataFlow(
            flow = foodAddToCartUseCase(param),
            onState = { state ->
                _state.update { it.copy(addToCartState = state) }

                if (state is LoadState.Success) {
                    emitEffect(DetailEffect.NavigateToCart)
                }
            }
        )
    }

    private fun toggleFavorite() {
        val isFavorite = _state.value.isFavorite
        val flow = if (isFavorite) {
            removeFavoriteFoodUseCase(foodId)
        } else {
            addFavoriteFoodUseCase(foodId)
        }

        observeDataFlow(
            flow = flow,
            onSuccess = {
                _state.update { it.copy(isFavorite = !isFavorite) }
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
                    onPrimary = { dismissDialog() }
                )
            )
        }
    }
}
