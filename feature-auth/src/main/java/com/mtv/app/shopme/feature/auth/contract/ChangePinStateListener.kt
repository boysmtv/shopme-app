/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChangePinStateListener.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.16
 */

package com.mtv.app.shopme.feature.auth.contract

data class ChangePinStateListener(
    val loading: Boolean = false,
    val error: String? = null
)

data class ChangePinDataListener(
    val oldPin: String = "",
    val newPin: String = "",
    val confirmPin: String = ""
)

data class ChangePinEventListener(
    val onOldPinChange: (String) -> Unit = {},
    val onNewPinChange: (String) -> Unit = {},
    val onConfirmPinChange: (String) -> Unit = {},
    val onSubmit: () -> Unit = {}
)

data class ChangePinNavigationListener(
    val onBack: () -> Unit = {}
)