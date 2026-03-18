/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: LoginContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.49
 */

package com.mtv.app.shopme.feature.auth.contract

import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.LoginResponse
import com.mtv.based.core.network.utils.Resource

data class LoginStateListener(
    val loginState: Resource<ApiResponse<LoginResponse>> = Resource.Loading
)

data class LoginDataListener(
    val email: String = "Boys.mtv@gmail.com",
    val password: String = "Mbi123456.",
)

//data class LoginDataListener(
//    val email: String = EMPTY_STRING,
//    val password: String = EMPTY_STRING
//)

data class LoginEventListener(
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onLoginClick: (String, String) -> Unit = { _, _ -> }
)

data class LoginNavigationListener(
    val onNavigateToHome: () -> Unit = {},
    val onNavigateToRegister: () -> Unit = {},
    val onNavigateToForgotPassword: () -> Unit = {},
)