/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: RegisterContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.52
 */

package com.mtv.app.shopme.feature.auth.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class RegisterStateListener(
    val registerState: ResourceFirebase<String> = ResourceFirebase.Loading
)

data class RegisterDataListener(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)

data class RegisterEventListener(
    val onEmailChange: (String) -> Unit,
    val onPasswordChange: (String) -> Unit,
    val onConfirmPasswordChange: (String) -> Unit,
    val onRegisterClick: () -> Unit
)

data class RegisterNavigationListener(
    val onNavigateToLogin: () -> Unit,
    val onBack: () -> Unit
)