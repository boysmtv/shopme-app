/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerPaymentMethodContract.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 22.17
 */

package com.mtv.app.shopme.feature.seller.contract

import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class SellerPaymentMethodStateListener(
    val isLoading: Boolean = false
)

data class SellerPaymentMethodDataListener(
    val cashEnabled: Boolean = false,
    val bankEnabled: Boolean = false,
    val bankNumber: String = EMPTY_STRING,
    val ovoEnabled: Boolean = false,
    val ovoNumber: String = EMPTY_STRING,
    val danaEnabled: Boolean = false,
    val danaNumber: String = EMPTY_STRING,
    val gopayEnabled: Boolean = false,
    val gopayNumber: String = EMPTY_STRING
)
data class SellerPaymentMethodEventListener(
    val onCashToggle: (Boolean) -> Unit = {},
    val onBankToggle: (Boolean) -> Unit = {},
    val onBankChange: (String) -> Unit = {},
    val onOvoToggle: (Boolean) -> Unit = {},
    val onOvoChange: (String) -> Unit = {},
    val onDanaToggle: (Boolean) -> Unit = {},
    val onDanaChange: (String) -> Unit = {},
    val onGopayToggle: (Boolean) -> Unit = {},
    val onGopayChange: (String) -> Unit = {},
    val onSave: () -> Unit = {}
)

data class SellerPaymentMethodNavigationListener(
    val navigateBack: () -> Unit = {}
)