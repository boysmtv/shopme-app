/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: PasswordContract.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 09.24
 */

package com.mtv.app.shopme.feature.auth.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class PasswordStateListener(
    val changePasswordState: ResourceFirebase<String> = ResourceFirebase.Loading
)

data class PasswordDataListener(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = ""
)

data class PasswordEventListener(
    val onCurrentPasswordChange: (String) -> Unit,
    val onNewPasswordChange: (String) -> Unit,
    val onConfirmPasswordChange: (String) -> Unit,
    val onSubmitClick: () -> Unit
)

data class PasswordNavigationListener(
    val onBack: () -> Unit
)