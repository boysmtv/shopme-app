/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashContract.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.50
 */

package com.mtv.app.shopme.feature.auth.contract

import com.mtv.based.core.network.utils.Resource

data class SplashStateListener(
    val splashState: Resource<String> = Resource.Loading,
)

data class SplashEventListener(
    val doSplashScreen: () -> Unit,
)

data class SplashNavigationListener(
    val onNavigateToLogin: () -> Unit,
)
