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

    val agreeTerms: Boolean = false,
    val agreeFoodSafety: Boolean = false,
    val agreeLocation: Boolean = false
)

sealed class SellerCreateCafeTncEvent {
    object Load : SellerCreateCafeTncEvent()
    object DismissDialog : SellerCreateCafeTncEvent()

    data class AgreeTerms(val value: Boolean) : SellerCreateCafeTncEvent()
    data class AgreeFoodSafety(val value: Boolean) : SellerCreateCafeTncEvent()
    data class AgreeLocation(val value: Boolean) : SellerCreateCafeTncEvent()

    object Next : SellerCreateCafeTncEvent()
    object ClickBack : SellerCreateCafeTncEvent()
}

sealed class SellerCreateCafeTncEffect {
    object NavigateBack : SellerCreateCafeTncEffect()
    object NavigateNext : SellerCreateCafeTncEffect()
}