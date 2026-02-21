/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SecurityContract.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.11
 */

package com.mtv.app.shopme.feature.customer.contract

data class SecurityStateListener(
    val loading: Boolean = false
)

data class SecurityDataListener(
    val biometricEnabled: Boolean = false
)

data class SecurityEventListener(
    val onToggleBiometric: (Boolean) -> Unit = {},
    val onLogoutAllDevice: () -> Unit = {},
    val onDeleteAccount: () -> Unit = {}
)

data class SecurityNavigationListener(
    val onBack: () -> Unit = {},
    val onChangePassword: () -> Unit = {},
    val onChangePin: () -> Unit = {}
)