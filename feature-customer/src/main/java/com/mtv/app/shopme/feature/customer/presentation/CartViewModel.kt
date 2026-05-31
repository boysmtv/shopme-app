/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.46
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.domain.model.PaymentMethod
import com.mtv.app.shopme.domain.param.CartClearByCafeParam
import com.mtv.app.shopme.domain.param.CartQuantityParam
import com.mtv.app.shopme.domain.param.CreateOrderParam
import com.mtv.app.shopme.domain.param.VerifyPinParam
import com.mtv.app.shopme.domain.usecase.CreateOrderUseCase
import com.mtv.app.shopme.domain.usecase.DeleteCartByCafeIdUseCase
import com.mtv.app.shopme.domain.usecase.DeleteCartUseCase
import com.mtv.app.shopme.domain.usecase.GetCartUseCase
import com.mtv.app.shopme.domain.usecase.GetCustomerUseCase
import com.mtv.app.shopme.domain.usecase.GetSessionTokenUseCase
import com.mtv.app.shopme.domain.usecase.GetVerifyPinUseCase
import com.mtv.app.shopme.domain.usecase.UpdateCartQuantityUseCase
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
    private val sessionManager: SessionManager
) : BaseEventViewModel<CartEvent, CartEffect>() {

    private val _state = MutableStateFlow(CartUiState())
    val uiState = _state.asStateFlow()

    private var pendingCartIds: List<String> = emptyList()
    private var pendingPaymentMethod: PaymentMethod = PaymentMethod.CASH
    private var pendingSessionToken: String = ""
    private var isCreatingOrder: Boolean = false

    override fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.Load -> load()

            is CartEvent.DismissDialog -> dismissDialog()

            is CartEvent.CheckoutClicked -> checkoutClicked(event)

            is CartEvent.PinSubmitted -> pinSubmitted(event)

            is CartEvent.CheckoutCancelled -> resetCheckoutFlow()

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
            onState = { state ->
                _state.update { current ->
                    when (state) {
                        is LoadState.Loading -> {
                            if (current.cartItems is LoadState.Success) {
                                current.copy(isRefreshing = true)
                            } else {
                                current.copy(
                                    cartItems = LoadState.Loading,
                                    isRefreshing = false
                                )
                            }
                        }

                        is LoadState.Success -> {
                            current.copy(
                                cartItems = state,
                                isRefreshing = false
                            )
                        }

                        is LoadState.Error -> {
                            if (current.cartItems is LoadState.Success) {
                                current.copy(isRefreshing = false)
                            } else {
                                current.copy(
                                    cartItems = state,
                                    isRefreshing = false
                                )
                            }
                        }

                        else -> {
                            current.copy(
                                cartItems = state,
                                isRefreshing = false
                            )
                        }
                    }
                }
            },
            onError = { error ->
                _state.update {
                    it.copy(isRefreshing = false)
                }
                showError(error)
            }
        )
    }

    private fun checkoutClicked(event: CartEvent.CheckoutClicked) {
        if (event.cartIds.isEmpty()) {
            emitEffect(
                CartEffect.ShowSnackbar(
                    message = "Keranjang masih kosong"
                )
            )
            return
        }

        pendingCartIds = event.cartIds
        pendingPaymentMethod = event.payment
        pendingSessionToken = ""
        isCreatingOrder = false

        ensureCustomerReadyForPurchase {
            getSessionForCheckout()
        }
    }

    private fun ensureCustomerReadyForPurchase(
        onReady: () -> Unit
    ) {
        var handled = false

        observeIndependentDataFlow(
            flow = customerUseCase(forceRefresh = true),
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
            onError = { error ->
                if (handled) return@observeIndependentDataFlow
                handled = true

                showError(error)
            }
        )
    }

    private fun getSessionForCheckout() {
        var handled = false

        observeDataFlow(
            flow = getSessionTokenUseCase(),
            onLoad = {
                setCheckoutLoading(true)
            },
            onState = { state ->
                when (state) {
                    is LoadState.Success -> {
                        if (handled) return@observeDataFlow
                        handled = true

                        setCheckoutLoading(false)

                        pendingSessionToken = state.data.token

                        emitEffect(CartEffect.OpenPinSheet)
                    }

                    is LoadState.Error -> {
                        if (handled) return@observeDataFlow
                        handled = true

                        setCheckoutLoading(false)
                        resetCheckoutFlow()
                        showError(state.error)
                    }

                    else -> Unit
                }
            },
            onError = { error ->
                if (handled) return@observeDataFlow
                handled = true

                setCheckoutLoading(false)
                resetCheckoutFlow()
                showError(error)
            }
        )
    }

    private fun pinSubmitted(event: CartEvent.PinSubmitted) {
        if (pendingSessionToken.isBlank()) {
            resetCheckoutFlow()

            emitEffect(
                CartEffect.ShowSnackbar(
                    message = "Sesi checkout sudah berakhir, silakan checkout ulang"
                )
            )
            return
        }

        verifyPin(
            token = pendingSessionToken,
            pin = event.pin
        )
    }

    private fun verifyPin(
        token: String,
        pin: String
    ) {
        var handled = false

        observeDataFlow(
            flow = verifyPinUseCase(
                VerifyPinParam(
                    token = token,
                    pin = pin
                )
            ),
            onLoad = {
                setCheckoutLoading(true)
            },
            onState = { state ->
                when (state) {
                    is LoadState.Success -> {
                        if (handled) return@observeDataFlow
                        handled = true

                        setCheckoutLoading(false)
                        createOrderAfterPinVerified()
                    }

                    is LoadState.Error -> {
                        if (handled) return@observeDataFlow
                        handled = true

                        setCheckoutLoading(false)

                        showError(
                            error = state.error,
                            allowBusinessUnauthorized = true
                        )

                        emitEffect(CartEffect.OpenPinSheet)
                    }

                    else -> Unit
                }
            },
            onError = { error ->
                if (handled) return@observeDataFlow
                handled = true

                setCheckoutLoading(false)

                showError(
                    error = error,
                    allowBusinessUnauthorized = true
                )

                emitEffect(CartEffect.OpenPinSheet)
            }
        )
    }

    private fun createOrderAfterPinVerified() {
        if (isCreatingOrder) return

        if (pendingCartIds.isEmpty() || pendingSessionToken.isBlank()) {
            resetCheckoutFlow()

            emitEffect(
                CartEffect.ShowSnackbar(
                    message = "Data checkout tidak valid, silakan checkout ulang"
                )
            )
            return
        }

        isCreatingOrder = true

        createOrder(
            cartIds = pendingCartIds,
            payment = pendingPaymentMethod,
            token = pendingSessionToken
        )
    }

    private fun createOrder(
        cartIds: List<String>,
        payment: PaymentMethod,
        token: String
    ) {
        var handled = false

        observeDataFlow(
            flow = createOrderUseCase(
                CreateOrderParam(
                    cartId = cartIds,
                    payment = payment,
                    token = token
                )
            ),
            onLoad = {
                setCheckoutLoading(true)
            },
            onState = { state ->
                when (state) {
                    is LoadState.Success -> {
                        if (handled) return@observeDataFlow
                        handled = true

                        setCheckoutLoading(false)
                        resetCheckoutFlow()

                        emitEffect(CartEffect.ShowSuccessDialog)
                    }

                    is LoadState.Error -> {
                        if (handled) return@observeDataFlow
                        handled = true

                        setCheckoutLoading(false)
                        resetCheckoutFlow()
                        showError(state.error)
                    }

                    else -> Unit
                }
            },
            onError = { error ->
                if (handled) return@observeDataFlow
                handled = true

                setCheckoutLoading(false)
                resetCheckoutFlow()
                showError(error)
            }
        )
    }

    private fun changeQuantity(event: CartEvent.ChangeQuantity) {
        val currentState = uiState.value.cartItems

        if (currentState !is LoadState.Success) return

        val previousItems = currentState.data

        val updatedItems = previousItems.mapNotNull { item ->
            if (item.id == event.cartId) {
                if (event.quantity <= 0) null else item.copy(quantity = event.quantity)
            } else {
                item
            }
        }

        _state.update {
            it.copy(
                cartItems = LoadState.Success(updatedItems)
            )
        }

        observeDataFlow(
            flow = cartQuantityUseCase(
                CartQuantityParam(
                    cartId = event.cartId,
                    quantity = event.quantity
                )
            ),
            onLoad = {
                showLoading()
            },
            onSuccess = {
                hideLoading()
            },
            onError = { error ->
                _state.update {
                    it.copy(
                        cartItems = LoadState.Success(previousItems)
                    )
                }

                showError(error)
            }
        )
    }

    private fun clearCartByCafe(event: CartEvent.ClearCartByCafe) {
        observeDataFlow(
            flow = clearCartByCafeIdUseCase(
                CartClearByCafeParam(
                    cafeId = event.cafeId
                )
            ),
            onLoad = {
                showLoading()
            },
            onSuccess = {
                hideLoading()
            },
            onError = { error ->
                showError(error)
            }
        )
    }

    private fun clearCart() {
        observeDataFlow(
            flow = clearCartUseCase(),
            onLoad = {
                showLoading()
            },
            onSuccess = {
                hideLoading()
            },
            onError = { error ->
                showError(error)
            }
        )
    }

    private fun setCheckoutLoading(
        isLoading: Boolean
    ) {
        _state.update {
            it.copy(
                isCheckoutLoading = isLoading
            )
        }
    }

    private fun resetCheckoutFlow() {
        pendingCartIds = emptyList()
        pendingPaymentMethod = PaymentMethod.CASH
        pendingSessionToken = ""
        isCreatingOrder = false

        setCheckoutLoading(false)
    }

    private fun showError(
        error: UiError,
        allowBusinessUnauthorized: Boolean = false
    ) {
        handleSessionError(
            error = error,
            sessionManager = sessionManager,
            beforeLogout = {
                hideLoading()
                setCheckoutLoading(false)
            },
            shouldForceLogout = { currentError ->
                currentError is UiError.Unauthorized &&
                        (
                                !allowBusinessUnauthorized ||
                                        !currentError.message.contains(
                                            other = "pin",
                                            ignoreCase = true
                                        )
                                )
            }
        ) { safeError ->
            setDialog(
                UiDialog.Center(
                    state = DialogStateV1(
                        type = DialogType.ERROR,
                        title = ErrorMessages.GENERIC_ERROR,
                        message = safeError.message
                    ),
                    onPrimary = {
                        dismissDialog()
                    }
                )
            )
        }
    }

    private fun showPurchaseRequirementsDialog(
        message: String
    ) {
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