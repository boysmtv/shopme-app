/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerPaymentMethodContract.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 22.17
 */

package com.mtv.app.shopme.feature.seller.contract

data class SellerPaymentMethodUiState(
    val isLoading: Boolean = false,

    val cashEnabled: Boolean = false,

    val bankEnabled: Boolean = false,
    val bankNumber: String = "",

    val ovoEnabled: Boolean = false,
    val ovoNumber: String = "",

    val danaEnabled: Boolean = false,
    val danaNumber: String = "",

    val gopayEnabled: Boolean = false,
    val gopayNumber: String = ""
)

sealed class SellerPaymentMethodEvent {
    object Load : SellerPaymentMethodEvent()
    object DismissDialog : SellerPaymentMethodEvent()

    data class ToggleCash(val value: Boolean) : SellerPaymentMethodEvent()

    data class ToggleBank(val value: Boolean) : SellerPaymentMethodEvent()
    data class ChangeBank(val value: String) : SellerPaymentMethodEvent()

    data class ToggleOvo(val value: Boolean) : SellerPaymentMethodEvent()
    data class ChangeOvo(val value: String) : SellerPaymentMethodEvent()

    data class ToggleDana(val value: Boolean) : SellerPaymentMethodEvent()
    data class ChangeDana(val value: String) : SellerPaymentMethodEvent()

    data class ToggleGopay(val value: Boolean) : SellerPaymentMethodEvent()
    data class ChangeGopay(val value: String) : SellerPaymentMethodEvent()

    object Save : SellerPaymentMethodEvent()

    object ClickBack : SellerPaymentMethodEvent()
}

sealed class SellerPaymentMethodEffect {
    object NavigateBack : SellerPaymentMethodEffect()
    object SaveSuccess : SellerPaymentMethodEffect()
}