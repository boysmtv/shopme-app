/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.contract

import com.mtv.based.core.network.utils.ResourceFirebase

data class SplashStateListener(
    val emptyState: ResourceFirebase<Unit> = ResourceFirebase.Loading
)

data class SplashDataListener(
    val emptyData: String? = null
)

data class SplashEventListener(
    val onDismissActiveDialog: () -> Unit
)

data class SplashNavigationListener(
    val onNavigateToHome: () -> Unit,
)
