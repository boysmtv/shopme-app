/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeTncContract.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 13.09
 */

package com.mtv.app.shopme.feature.seller.contract

data class SellerCreateCafeTncUiState(
    val isLoading: Boolean = false,
    val title: String = "",
    val description: String = "",
    val footer: String = "",
    val terms: List<SellerCreateCafeTncItemUiState> = emptyList()
)

data class SellerCreateCafeTncItemUiState(
    val id: String,
    val title: String,
    val description: String,
    val checked: Boolean = false
)

sealed class SellerCreateCafeTncEvent {
    object Load : SellerCreateCafeTncEvent()
    object DismissDialog : SellerCreateCafeTncEvent()

    data class ToggleTerm(val id: String, val value: Boolean) : SellerCreateCafeTncEvent()

    object Next : SellerCreateCafeTncEvent()
    object ClickBack : SellerCreateCafeTncEvent()
}

sealed class SellerCreateCafeTncEffect {
    object NavigateBack : SellerCreateCafeTncEffect()
    object NavigateNext : SellerCreateCafeTncEffect()
}
