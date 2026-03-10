/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: RegisterContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.52
 */

package com.mtv.app.shopme.feature.auth.contract

import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.based.core.network.utils.Resource
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.ERROR_STRING

data class RegisterStateListener(
    val registerState: Resource<ApiResponse<Unit>> = Resource.Loading,
    val activeDialog: RegisterDialog? = null
)

data class RegisterDataListener(
    val name: String = "Dedy Wijaya",
    val email: String = "Boys.mtv@gmail.com",
    val password: String = "Mbi123456.",
)

//data class RegisterDataListener(
//    val name: String = EMPTY_STRING,
//    val email: String = EMPTY_STRING,
//    val password: String = EMPTY_STRING,
//)

data class RegisterEventListener(
    val onNameChange: (String) -> Unit = {},
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onRegisterClick: (String, String, String) -> Unit = { _, _, _ -> },
    val onDismissActiveDialog: () -> Unit = {}
)

data class RegisterNavigationListener(
    val onNavigateToLogin: () -> Unit = {},
    val onBack: () -> Unit = {}
)

sealed class RegisterDialog {

    data class Error(
        val message: String = ERROR_STRING
    ) : RegisterDialog()

    object Success : RegisterDialog()

}