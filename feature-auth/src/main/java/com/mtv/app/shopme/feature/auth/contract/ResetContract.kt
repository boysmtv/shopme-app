/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ResetContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.54
 */

package com.mtv.app.shopme.feature.auth.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class ResetStateListener(
    val resetState: ResourceFirebase<String> = ResourceFirebase.Loading
)

data class ResetDataListener(
    val email: String = ""
)

data class ResetEventListener(
    val onEmailChange: (String) -> Unit,
    val onResetClick: () -> Unit
)

data class ResetNavigationListener(
    val onNavigateToLogin: () -> Unit,
    val onBack: () -> Unit
)