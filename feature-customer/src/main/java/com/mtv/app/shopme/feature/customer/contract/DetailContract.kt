/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DetailContract.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 08.59
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.data.remote.request.CartVariantRequest
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.based.core.network.utils.LoadState

data class DetailUiState(
    val food: LoadState<FoodResponse> = LoadState.Loading,
    val similarFoods: LoadState<List<FoodResponse>> = LoadState.Idle,
    val addToCartState: LoadState<Unit> = LoadState.Idle
)

sealed class DetailEvent {
    object BackClicked : DetailEvent()
    object ChatClicked : DetailEvent()
    object OpenCart : DetailEvent()
    data class AddToCart(
        val foodId: String,
        val variants: List<CartVariantRequest>,
        val quantity: Int,
        val note: String
    ) : DetailEvent()
    data class ClickCafe(val cafeId: String) : DetailEvent()
    data class ClickSimilarFood(val foodId: String) : DetailEvent()
}

sealed class DetailEffect {
    object NavigateBack : DetailEffect()
    object NavigateToCart : DetailEffect()
    object NavigateToChat : DetailEffect()
    data class NavigateToCafe(val cafeId: String) : DetailEffect()
    data class NavigateToDetail(val foodId: String) : DetailEffect()
}