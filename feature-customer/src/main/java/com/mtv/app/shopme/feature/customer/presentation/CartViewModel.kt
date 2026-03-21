/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.46
 */

package com.mtv.app.shopme.feature.customer.presentation

import androidx.lifecycle.viewModelScope
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.common.valueFlowOf
import com.mtv.app.shopme.data.dto.PaymentMethod
import com.mtv.app.shopme.data.remote.request.CartClearByCafeRequest
import com.mtv.app.shopme.data.remote.request.CartQuantityRequest
import com.mtv.app.shopme.data.remote.request.CreateOrderRequest
import com.mtv.app.shopme.data.remote.request.VerifyPinRequest
import com.mtv.app.shopme.data.remote.response.CartItemResponse
import com.mtv.app.shopme.domain.usecase.CartItemUseCase
import com.mtv.app.shopme.domain.usecase.CartQuantityUseCase
import com.mtv.app.shopme.domain.usecase.ClearCartByCafeIdUseCase
import com.mtv.app.shopme.domain.usecase.ClearCartUseCase
import com.mtv.app.shopme.domain.usecase.CreateOrderUseCase
import com.mtv.app.shopme.domain.usecase.GetSessionTokenUseCase
import com.mtv.app.shopme.domain.usecase.VerifyPinUseCase
import com.mtv.app.shopme.feature.customer.contract.CartDataListener
import com.mtv.app.shopme.feature.customer.contract.CartStateListener
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.core.provider.based.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartItemUseCase: CartItemUseCase,
    private val getSessionTokenUseCase: GetSessionTokenUseCase,
    private val verifyPinUseCase: VerifyPinUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val cartQuantityUseCase: CartQuantityUseCase,
    private val clearCartByCafeIdUseCase: ClearCartByCafeIdUseCase,
    private val clearCartUseCase: ClearCartUseCase,
) : BaseViewModel(), UiOwner<CartStateListener, CartDataListener> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(CartStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(CartDataListener())

    private var previousCartItems: List<CartItemResponse> = emptyList()

    init {
        doFetchCart()
        observeQuantityState()
    }

    fun doFetchCart() {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.cartState },
                set = { copy(cartState = it) }
            ),
            block = {
                cartItemUseCase(Unit)
            },
            onSuccess = { response ->
                uiData.update {
                    it.copy(cartItems = response.data)
                }
            }
        )
    }

    fun doGetSession() {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.sessionTokenState },
                set = { copy(sessionTokenState = it) }
            ),
            block = {
                getSessionTokenUseCase(Unit)
            },
            onSuccess = { response ->
                uiData.update {
                    it.copy(sessionToken = response.data)
                }
            }
        )
    }

    fun doVerifyPin(
        token: String,
        pin: String
    ) {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.verifyPinState },
                set = { state -> copy(verifyPinState = state) }
            ),
            block = {
                verifyPinUseCase(
                    VerifyPinRequest(
                        token = token,
                        pin = pin
                    )
                )
            },
            onSuccess = {

            }
        )
    }

    fun doCreateOrder(
        cartIds: List<String>,
        paymentMethod: PaymentMethod,
        token: String,
    ) {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.createOrderState },
                set = { state -> copy(createOrderState = state) }
            ),
            block = {
                createOrderUseCase(
                    CreateOrderRequest(
                        cartId = cartIds,
                        payment = paymentMethod,
                        token = token
                    )
                )
            },
            onSuccess = {

            }
        )
    }

    fun doPostQuantity(
        cartId: String,
        quantity: Int
    ) {
        previousCartItems = uiData.value.cartItems.orEmpty()

        uiData.update { current ->
            val updated = current.cartItems
                ?.mapNotNull { item ->
                    if (item.id == cartId) {
                        if (quantity <= 0) null
                        else item.copy(quantity = quantity)
                    } else item
                }

            current.copy(cartItems = updated)
        }

        launchUseCase(
            loading = false,
            target = uiState.valueFlowOf(
                get = { it.quantityState },
                set = { copy(quantityState = it) }
            ),
            block = {
                cartQuantityUseCase(
                    CartQuantityRequest(
                        cartId = cartId,
                        quantity = quantity
                    )
                )
            },
            onSuccess = {
                doFetchCart()
            }
        )
    }

    private fun observeQuantityState() {
        viewModelScope.launch {
            uiState.collect { state ->
                when (state.quantityState) {

                    is Resource.Error -> {
                        uiData.update {
                            it.copy(cartItems = previousCartItems)
                        }

                        uiState.update {
                            it.copy(quantityState = Resource.Idle)
                        }
                    }

                    is Resource.Success -> {
                        uiState.update {
                            it.copy(quantityState = Resource.Idle)
                        }
                    }

                    else -> Unit
                }
            }
        }
    }

    fun doClearCartByCafe(
        cafeId: String,
    ) {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.clearCartByCafeState },
                set = { state -> copy(clearCartByCafeState = state) }
            ),
            block = {
                clearCartByCafeIdUseCase(
                    CartClearByCafeRequest(
                        cafeId = cafeId
                    )
                )
            },
            onSuccess = {
                doFetchCart()
            }
        )
    }

    fun doClearCart() {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.clearCartState },
                set = { state -> copy(clearCartState = state) }
            ),
            block = {
                clearCartUseCase(Unit)
            },
            onSuccess = {
                doFetchCart()
            }
        )
    }

    fun resetSessionTokenState() {
        uiState.update {
            it.copy(sessionTokenState = Resource.Idle)
        }
    }

    fun resetVerifyPinState() {
        uiState.update {
            it.copy(verifyPinState = Resource.Idle)
        }
    }

    fun doDismissActiveDialog() {
        uiState.update {
            it.copy(
                activeDialog = null,
            )
        }
    }

}