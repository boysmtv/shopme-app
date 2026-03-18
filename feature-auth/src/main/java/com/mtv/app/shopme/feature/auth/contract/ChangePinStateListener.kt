/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChangePinStateListener.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.16
 */

package com.mtv.app.shopme.feature.auth.contract

import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class ChangePinStateListener(
    val loading: Boolean = false,
    val error: String? = null
)

data class ChangePinDataListener(
    val oldPin: String = EMPTY_STRING,
    val newPin: String = EMPTY_STRING,
    val confirmPin: String = EMPTY_STRING
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