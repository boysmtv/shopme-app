/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: LoginContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.49
 */

package com.mtv.app.shopme.feature.auth.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class LoginStateListener(
    val loginState: ResourceFirebase<String> = ResourceFirebase.Loading
)

data class LoginDataListener(
    val email: String = "",
    val password: String = ""
)

data class LoginEventListener(
    val onEmailChange: (String) -> Unit,
    val onPasswordChange: (String) -> Unit,
    val onLoginClick: () -> Unit
)

data class LoginNavigationListener(
    val onNavigateToRegister: () -> Unit,
    val onNavigateToForgotPassword: () -> Unit,
    val onBack: () -> Unit
)