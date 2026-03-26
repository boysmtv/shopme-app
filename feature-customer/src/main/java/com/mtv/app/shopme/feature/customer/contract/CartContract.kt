/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.domain.model.CartItem
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.model.SessionToken
import com.mtv.based.core.network.utils.LoadState

data class CartUiState(
    val cartItems: LoadState<List<CartItem>> = LoadState.Idle,
    val sessionToken: LoadState<SessionToken> = LoadState.Idle
)

sealed class CartEvent {
    object Load : CartEvent()
    object DismissDialog : CartEvent()

    object GetSession : CartEvent()

    data class VerifyPin(
        val token: String,
        val pin: String
    ) : CartEvent()

    data class CreateOrder(
        val cartIds: List<String>,
        val payment: PaymentMethod,
        val token: String
    ) : CartEvent()

    data class ChangeQuantity(
        val cartId: String,
        val quantity: Int
    ) : CartEvent()

    data class ClearCartByCafe(val cafeId: String) : CartEvent()
    object ClearCart : CartEvent()
}

sealed class CartEffect {
    object OpenPinSheet : CartEffect()
    object NavigateToOrder : CartEffect()
}