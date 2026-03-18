/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartViewModel.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.46
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.based.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.common.valueFlowOf
import com.mtv.app.shopme.data.remote.request.CartInquiryRequest
import com.mtv.app.shopme.data.remote.request.CartQuantityRequest
import com.mtv.app.shopme.data.remote.request.CartValidateRequest
import com.mtv.app.shopme.data.remote.request.VerifyPinRequest
import com.mtv.app.shopme.domain.usecase.CartInquiryUseCase
import com.mtv.app.shopme.domain.usecase.CartItemUseCase
import com.mtv.app.shopme.domain.usecase.CartQuantityUseCase
import com.mtv.app.shopme.domain.usecase.CartValidateUseCase
import com.mtv.app.shopme.domain.usecase.VerifyPinUseCase
import com.mtv.app.shopme.feature.customer.contract.CartDataListener
import com.mtv.app.shopme.feature.customer.contract.CartDialog
import com.mtv.app.shopme.feature.customer.contract.CartStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartItemUseCase: CartItemUseCase,
    private val cartQuantityUseCase: CartQuantityUseCase,
    private val cartInquiryUseCase: CartInquiryUseCase,
    private val cartValidateUseCase: CartValidateUseCase,
    private val verifyPinUseCase: VerifyPinUseCase,
) : BaseViewModel(), UiOwner<CartStateListener, CartDataListener> {

    /** UI STATE : LOADING / ERROR / SUCCESS (API Response) */
    override val uiState = MutableStateFlow(CartStateListener())

    /** UI DATA : DATA PERSIST (Prefs) */
    override val uiData = MutableStateFlow(CartDataListener())

    init {
        //doFetchCart()
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

    fun doPostQuantity(
        cartId: String,
        quantity: Int
    ) {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.quantityState },
                set = { state -> copy(quantityState = state) }
            ),
            block = {
                cartQuantityUseCase(
                    CartQuantityRequest(
                        cartId = cartId,
                        quantity = quantity
                    )
                )
            }
        )
    }

    fun doPostInquiry(
        cartId: String,
    ) {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.inquiryState },
                set = { state -> copy(inquiryState = state) }
            ),
            block = {
                cartInquiryUseCase(
                    CartInquiryRequest(
                        cartId = cartId,
                    )
                )
            }
        )
    }

    fun doPostValidate(
        cartId: String,
    ) {
        launchUseCase(
            target = uiState.valueFlowOf(
                get = { it.validateState },
                set = { state -> copy(validateState = state) }
            ),
            block = {
                cartValidateUseCase(
                    CartValidateRequest(
                        cartId = cartId,
                    )
                )
            }
        )
    }

    fun doVerifyPin(
        trxId: String,
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
                        trxId = trxId,
                        pin = pin
                    )
                )
            },
            onSuccess = {
                uiState.update {
                    it.copy(
                        activeDialog = CartDialog.Success
                    )
                }
            }
        )
    }

    fun doDismissActiveDialog() {
        uiState.update {
            it.copy(
                activeDialog = null,
            )
        }
    }

}