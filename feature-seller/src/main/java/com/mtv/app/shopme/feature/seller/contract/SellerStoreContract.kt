/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerStoreContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 10.00
 */

package com.mtv.app.shopme.feature.seller.contract

import androidx.compose.runtime.Immutable

@Immutable
data class SellerStoreUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,

    val sellerName: String = "",
    val sellerPhoto: String = "",
    val email: String = "",
    val phone: String = "",

    val storeName: String = "",
    val storePhoto: String = "",
    val storeAddress: String = "",

    val isOnline: Boolean = true,

    val rating: Float = 0f,
    val ratingCount: Int = 0
)

sealed class SellerStoreEvent {
    object Load : SellerStoreEvent()
    object DismissDialog : SellerStoreEvent()

    object ToggleOnline : SellerStoreEvent()

    object ClickOrderHistory : SellerStoreEvent()
    object ClickEditProfile : SellerStoreEvent()
    object ClickStoreSettings : SellerStoreEvent()
    object ClickBankAccount : SellerStoreEvent()
    object ClickChangePassword : SellerStoreEvent()
    object ClickHelpCenter : SellerStoreEvent()

    object Logout : SellerStoreEvent()
    object ClickBack : SellerStoreEvent()
    object ClickShareQr : SellerStoreEvent()
    object DismissQrDialog : SellerStoreEvent()
}

sealed class SellerStoreEffect {
    object NavigateBack : SellerStoreEffect()
    object NavigateToDashboard : SellerStoreEffect()
    object NavigateToOrders : SellerStoreEffect()
    object NavigateToEditProfile : SellerStoreEffect()
    object NavigateToStoreSettings : SellerStoreEffect()
    object NavigateToBankAccount : SellerStoreEffect()
    object NavigateToChangePassword : SellerStoreEffect()
    object NavigateToHelpCenter : SellerStoreEffect()

    object LogoutSuccess : SellerStoreEffect()
}
