/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerStoreContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 10.00
 */

package com.mtv.app.shopme.feature.seller.contract

data class SellerStoreUiState(
    val isLoading: Boolean = false,

    val sellerName: String = "",
    val sellerPhoto: String = "",
    val email: String = "",
    val phone: String = "",

    val storeName: String = "",
    val storePhoto: String = "",
    val storeAddress: String = "",

    val isOnline: Boolean = true
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
    object ClickBackToCustomer : SellerStoreEvent()

    object Logout : SellerStoreEvent()
    object ClickBack : SellerStoreEvent()
}

sealed class SellerStoreEffect {
    object NavigateBack : SellerStoreEffect()
    object NavigateToOrders : SellerStoreEffect()
    object NavigateToEditProfile : SellerStoreEffect()
    object NavigateToStoreSettings : SellerStoreEffect()
    object NavigateToBankAccount : SellerStoreEffect()
    object NavigateToChangePassword : SellerStoreEffect()
    object NavigateToHelpCenter : SellerStoreEffect()
    object NavigateToCustomerHome : SellerStoreEffect()

    object LogoutSuccess : SellerStoreEffect()
}
