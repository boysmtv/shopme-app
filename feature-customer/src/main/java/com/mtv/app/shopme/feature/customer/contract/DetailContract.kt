/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DetailContract.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 08.59
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.domain.model.CartVariant
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.param.CartAddVariantParam
import com.mtv.based.core.network.utils.LoadState

data class DetailUiState(
    val food: LoadState<Food> = LoadState.Idle,
    val similarFoods: LoadState<List<Food>> = LoadState.Idle,
    val addToCartState: LoadState<Unit> = LoadState.Idle,
    val isFavorite: Boolean = false
)

sealed class DetailEvent {
    object Load : DetailEvent()
    object BackClicked : DetailEvent()
    object DismissDialog : DetailEvent()
    object ChatClicked : DetailEvent()
    object ToggleFavorite : DetailEvent()
    object OpenCart : DetailEvent()

    data class AddToCart(
        val foodId: String,
        val variants: List<CartAddVariantParam>,
        val quantity: Int,
        val note: String
    ) : DetailEvent()

    data class ClickCafe(val cafeId: String) : DetailEvent()
    data class ClickSimilarFood(val foodId: String) : DetailEvent()
}

sealed class DetailEffect {
    object NavigateBack : DetailEffect()
    object NavigateToCart : DetailEffect()
    object NavigateToEditProfile : DetailEffect()
    data class NavigateToChat(val chatId: String) : DetailEffect()
    data class NavigateToCafe(val cafeId: String) : DetailEffect()
    data class NavigateToDetail(val foodId: String) : DetailEffect()
}
