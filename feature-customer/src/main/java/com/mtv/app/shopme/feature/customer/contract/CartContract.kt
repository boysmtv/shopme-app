/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.data.dto.PaymentMethod
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.CartItemResponse
import com.mtv.app.shopme.data.remote.response.SessionTokenResponse
import com.mtv.app.shopme.data.remote.response.VerifyPinResponse
import com.mtv.based.core.network.utils.Resource

sealed class CartUiEvent {
    data object OpenPinSheet : CartUiEvent()
    data object TriggerCreateOrder : CartUiEvent()
    data object NavigateToOrder : CartUiEvent()
}

data class CartStateListener(
    val cartState: Resource<ApiResponse<List<CartItemResponse>>> = Resource.Loading,
    val sessionTokenState: Resource<ApiResponse<SessionTokenResponse>> = Resource.Loading,
    val verifyPinState: Resource<ApiResponse<VerifyPinResponse>> = Resource.Loading,
    val createOrderState: Resource<ApiResponse<Unit>> = Resource.Loading,
    val quantityState: Resource<ApiResponse<Unit>> = Resource.Loading,
    val clearCartState: Resource<ApiResponse<Unit>> = Resource.Loading,
    val clearCartByCafeState: Resource<ApiResponse<Unit>> = Resource.Loading,
    val activeDialog: CartDialog? = null
)

data class CartDataListener(
    val cartItems: List<CartItemResponse>? = null,
    val sessionToken: SessionTokenResponse? = null,
)

data class CartEventListener(
    val onGetSession: () -> Unit = {},
    val onResetSessionTokenState: () -> Unit = {},
    val onVerifyPin: (String, String) -> Unit = { _, _ -> },
    val onResetVerifyPinState: () -> Unit = {},
    val onCreateOrder: (List<String>, PaymentMethod, String) -> Unit = { _, _, _ -> },
    val onQuantityClick: (String, Int) -> Unit = { _, _ -> },
    val onClearFoodByCafe: (String) -> Unit = { _ -> },
    val onClearCart: () -> Unit = {},
    val onDismissActiveDialog: () -> Unit = {}
)

data class CartNavigationListener(
    val onNavigateToDetail: (String) -> Unit = {},
    val onNavigateToOrder: () -> Unit = {},
    val onBack: () -> Unit = {},
)

sealed class CartDialog {
    object Success : CartDialog()
}
