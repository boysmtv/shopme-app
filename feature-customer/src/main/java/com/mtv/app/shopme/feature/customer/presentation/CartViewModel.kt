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
import com.mtv.app.shopme.domain.usecase.GetCustomerUseCase
import com.mtv.app.shopme.domain.usecase.GetCartUseCase
import com.mtv.app.shopme.domain.usecase.UpdateCartQuantityUseCase
import com.mtv.app.shopme.domain.usecase.DeleteCartByCafeIdUseCase
import com.mtv.app.shopme.domain.usecase.DeleteCartUseCase
import com.mtv.app.shopme.domain.usecase.CreateOrderUseCase
import com.mtv.app.shopme.domain.usecase.GetSessionTokenUseCase
import com.mtv.app.shopme.domain.usecase.GetVerifyPinUseCase
import com.mtv.app.shopme.feature.customer.contract.CartEffect
import com.mtv.app.shopme.feature.customer.contract.CartEvent
import com.mtv.app.shopme.feature.customer.contract.CartUiState
import com.mtv.app.shopme.feature.customer.utils.purchaseRequirementsMessage
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
class CartViewModel @Inject constructor(
    private val cartItemUseCase: GetCartUseCase,
    private val customerUseCase: GetCustomerUseCase,
    private val getSessionTokenUseCase: GetSessionTokenUseCase,
    private val verifyPinUseCase: GetVerifyPinUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val cartQuantityUseCase: UpdateCartQuantityUseCase,
    private val clearCartByCafeIdUseCase: DeleteCartByCafeIdUseCase,
    private val clearCartUseCase: DeleteCartUseCase,
    private val sessionManager: SessionManager,
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
        ensureCustomerReadyForPurchase {
            performGetSession()
        }
    }

    private fun performGetSession() {
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

    private fun ensureCustomerReadyForPurchase(onReady: () -> Unit) {
        var handled = false
        observeIndependentDataFlow(
            flow = customerUseCase(),
            onSuccess = { customer ->
                if (handled) return@observeIndependentDataFlow
                handled = true
                val message = customer.purchaseRequirementsMessage()
                if (message == null) {
                    onReady()
                } else {
                    showPurchaseRequirementsDialog(message)
                }
            },
            onError = {
                if (handled) return@observeIndependentDataFlow
                handled = true
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
            onLoad = {
                _state.update { it.copy(verifyPin = LoadState.Loading) }
            },
            onSuccess = {
                _state.update { it.copy(verifyPin = LoadState.Success(Unit)) }
            },
            onError = {
                _state.update { error -> error.copy(verifyPin = LoadState.Error(it)) }
                showError(it, allowBusinessUnauthorized = true)
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
            onLoad = {
                _state.update { it.copy(createOrder = LoadState.Loading) }
            },
            onSuccess = {
                _state.update { it.copy(createOrder = LoadState.Success(Unit)) }
                emitEffect(CartEffect.NavigateToOrder)
            },
            onError = {
                _state.update { error -> error.copy(createOrder = LoadState.Error(it)) }
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

    private fun showError(
        error: UiError,
        allowBusinessUnauthorized: Boolean = false
    ) {
        handleSessionError(
            error = error,
            sessionManager = sessionManager,
            beforeLogout = { hideLoading() },
            shouldForceLogout = { currentError ->
                currentError is UiError.Unauthorized && (
                    !allowBusinessUnauthorized ||
                        !currentError.message.contains("pin", ignoreCase = true)
                    )
            }
        ) {
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

    private fun showPurchaseRequirementsDialog(message: String) {
        setDialog(
            UiDialog.Center(
                state = DialogStateV1(
                    type = DialogType.ERROR,
                    title = "Lengkapi profil dulu",
                    message = message
                ),
                onPrimary = {
                    dismissDialog()
                    emitEffect(CartEffect.NavigateToEditProfile)
                }
            )
        )
    }
}
