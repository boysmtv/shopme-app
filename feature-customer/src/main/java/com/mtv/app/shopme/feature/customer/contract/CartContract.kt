/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.customer.contract

import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.CartInquiryResponse
import com.mtv.app.shopme.data.remote.response.CartItemResponse
import com.mtv.app.shopme.data.remote.response.CartValidateResponse
import com.mtv.app.shopme.data.remote.response.VerifyPinResponse
import com.mtv.based.core.network.utils.Resource

data class CartStateListener(
    val cartState: Resource<ApiResponse<List<CartItemResponse>>> = Resource.Loading,
    val quantityState: Resource<ApiResponse<Unit>> = Resource.Loading,
    val inquiryState: Resource<ApiResponse<CartInquiryResponse>> = Resource.Loading,
    val validateState: Resource<ApiResponse<CartValidateResponse>> = Resource.Loading,
    val verifyPinState: Resource<ApiResponse<VerifyPinResponse>> = Resource.Loading,
    val activeDialog: CartDialog? = null
)

data class CartDataListener(
    val cartItems: List<CartItemResponse>? = null,
)

data class CartEventListener(
    val onQuantityClick: (String, Int) -> Unit = { _, _ -> },
    val onInquiry: (String) -> Unit = { },
    val onValidate: (String) -> Unit = { },
    val onVerifyPin: (String, String) -> Unit = { _, _ -> },
    val onDismissActiveDialog: () -> Unit = {}
)

data class CartNavigationListener(
    val onBack: () -> Unit = {},
    val onNavigateToOrder: () -> Unit = {}

)

sealed class CartDialog {
    object Success : CartDialog()
}
