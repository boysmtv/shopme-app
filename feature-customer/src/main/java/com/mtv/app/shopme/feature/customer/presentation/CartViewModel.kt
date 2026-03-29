/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.46
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.param.CartClearByCafeParam
import com.mtv.app.shopme.domain.param.CartQuantityParam
import com.mtv.app.shopme.domain.param.CreateOrderParam
import com.mtv.app.shopme.domain.param.VerifyPinParam
import com.mtv.app.shopme.domain.usecase.CartItemUseCase
import com.mtv.app.shopme.domain.usecase.CartQuantityUseCase
import com.mtv.app.shopme.domain.usecase.ClearCartByCafeIdUseCase
import com.mtv.app.shopme.domain.usecase.ClearCartUseCase
import com.mtv.app.shopme.domain.usecase.CreateOrderUseCase
import com.mtv.app.shopme.domain.usecase.GetSessionTokenUseCase
import com.mtv.app.shopme.domain.usecase.VerifyPinUseCase
import com.mtv.app.shopme.feature.customer.contract.CartEffect
import com.mtv.app.shopme.feature.customer.contract.CartEvent
import com.mtv.app.shopme.feature.customer.contract.CartUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartItemUseCase: CartItemUseCase,
    private val getSessionTokenUseCase: GetSessionTokenUseCase,
    private val verifyPinUseCase: VerifyPinUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val cartQuantityUseCase: CartQuantityUseCase,
    private val clearCartByCafeIdUseCase: ClearCartByCafeIdUseCase,
    private val clearCartUseCase: ClearCartUseCase,
) : BaseEventViewModel<CartEvent, CartEffect>() {

    private val _state = MutableStateFlow(CartUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.Load -> load()
            is CartEvent.DismissDialog -> dismissDialog()
            is CartEvent.GetSession -> getSession()
            is CartEvent.VerifyPin -> verifyPin(event)
            is CartEvent.CreateOrder -> createOrder(event)
            is CartEvent.ChangeQuantity -> changeQuantity(event)
            is CartEvent.ClearCartByCafe -> clearCartByCafe(event)
            is CartEvent.ClearCart -> clearCart()
        }
    }

    private fun load() {
        observeCart()
    }

    private fun observeCart() {
        observeDataFlow(
            flow = cartItemUseCase(),
            onLoad = { showLoading() },
            onSuccess = { hideLoading() },
            onState = { state ->
                _state.update {
                    it.copy(cartItems = state)
                }
            },
            onError = {
                showError(it)
            }
        )
    }

    private fun getSession() {
        observeDataFlow(
            flow = getSessionTokenUseCase(),
            onLoad = { showLoading() },
            onSuccess = { hideLoading() },
            onState = { state ->
                _state.update {
                    it.copy(sessionToken = state)
                }
            },
            onError = {
                showError(it)
            }
        )
    }

    private fun verifyPin(event: CartEvent.VerifyPin) {
        observeDataFlow(
            flow = verifyPinUseCase(
                VerifyPinParam(
                    token = event.token,
                    pin = event.pin
                )
            ),
            onLoad = { showLoading() },
            onSuccess = { hideLoading() },
            onError = {
                showError(it)
            }
        )
    }

    private fun createOrder(event: CartEvent.CreateOrder) {
        observeDataFlow(
            flow = createOrderUseCase(
                CreateOrderParam(
                    cartId = event.cartIds,
                    payment = event.payment,
                    token = event.token
                )
            ),
            onLoad = { showLoading() },
            onSuccess = { hideLoading() },
            onError = {
                showError(it)
            }
        )
    }

    private fun changeQuantity(event: CartEvent.ChangeQuantity) {
        val currentState = uiState.value.cartItems

        if (currentState !is LoadState.Success) return

        val previousItems = currentState.data

        val updatedItems = previousItems.mapNotNull { item ->
            if (item.id == event.cartId) {
                if (event.quantity <= 0) null
                else item.copy(quantity = event.quantity)
            } else item
        }

        _state.update {
            it.copy(cartItems = LoadState.Success(updatedItems))
        }

        observeDataFlow(
            flow = cartQuantityUseCase(
                CartQuantityParam(
                    cartId = event.cartId,
                    quantity = event.quantity
                )
            ),
            onLoad = { showLoading() },
            onSuccess = { hideLoading() },
            onError = { error ->
                _state.update {
                    it.copy(cartItems = LoadState.Success(previousItems))
                }
                showError(error)
            }
        )
    }

    private fun clearCartByCafe(event: CartEvent.ClearCartByCafe) {
        observeDataFlow(
            flow = clearCartByCafeIdUseCase(
                CartClearByCafeParam(
                    event.cafeId
                )
            ),
            onLoad = { showLoading() },
            onSuccess = { hideLoading() },
            onError = {
                showError(it)
            }
        )
    }

    private fun clearCart() {
        observeDataFlow(
            flow = clearCartUseCase(),
            onLoad = { showLoading() },
            onSuccess = { hideLoading() },
            onError = {
                showError(it)
            }
        )
    }

    private fun showError(error: UiError) {
        setDialog(
            UiDialog.Center(
                state = DialogStateV1(
                    type = DialogType.ERROR,
                    title = ErrorMessages.GENERIC_ERROR,
                    message = error.message
                ),
                onPrimary = { dismissDialog() }
            )
        )
    }
}