/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.feature.auth.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.auth.contract.SplashDataListener
import com.mtv.app.shopme.feature.auth.contract.SplashEventListener
import com.mtv.app.shopme.feature.auth.contract.SplashNavigationListener
import com.mtv.app.shopme.feature.auth.contract.SplashStateListener
import com.mtv.app.shopme.feature.auth.presentation.SplashViewModel
import com.mtv.app.shopme.feature.auth.ui.SplashScreen
import com.mtv.app.shopme.nav.AuthDestinations
import com.mtv.app.shopme.nav.CustomerDestinations
import com.mtv.app.shopme.nav.navigateAndPopSplash

@Composable
fun SplashRoute(nav: NavController) {
    BaseRoute<SplashViewModel, SplashStateListener, SplashDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            SplashScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = splashEvent(vm),
                uiNavigation = splashNavigation(nav)
            )
        }
    }
}

private fun splashEvent(vm: SplashViewModel) = SplashEventListener(
    onDismissActiveDialog = {}
)

private fun splashNavigation(nav: NavController) = SplashNavigationListener(
    onNavigateToLogin = {
        nav.navigateAndPopSplash(AuthDestinations.LOGIN_GRAPH)
    }
)
