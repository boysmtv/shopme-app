/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.domain.model.Cart
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.based.core.network.utils.LoadState

data class CartUiState(
    val cartItems: LoadState<List<Cart>> = LoadState.Idle,
    val isRefreshing: Boolean = false,
    val isCheckoutLoading: Boolean = false
)

sealed class CartEvent {

    object Load : CartEvent()

    object DismissDialog : CartEvent()

    data class CheckoutClicked(
        val cartIds: List<String>,
        val payment: PaymentMethod
    ) : CartEvent()

    data class PinSubmitted(
        val pin: String
    ) : CartEvent()

    object CheckoutCancelled : CartEvent()

    data class ChangeQuantity(
        val cartId: String,
        val quantity: Int
    ) : CartEvent()

    data class ClearCartByCafe(
        val cafeId: String
    ) : CartEvent()

    object ClearCart : CartEvent()
}

sealed class CartEffect {

    /**
     * Local UI effect.
     * Handled by CartScreen.
     */
    object OpenPinSheet : CartEffect()

    /**
     * Local UI effect.
     * Handled by CartScreen.
     */
    object ShowSuccessDialog : CartEffect()

    /**
     * Navigation effect.
     * Handled by CartScreen through callback from CartRoute.
     */
    object NavigateToOrder : CartEffect()

    /**
     * Navigation effect.
     * Handled by CartScreen through callback from CartRoute.
     */
    object NavigateToEditProfile : CartEffect()

    /**
     * Local UI effect.
     * Handled by CartScreen.
     */
    data class ShowSnackbar(
        val message: String
    ) : CartEffect()
}