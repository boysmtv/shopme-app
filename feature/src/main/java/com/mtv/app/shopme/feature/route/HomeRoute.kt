/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: HomeRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.feature.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.contract.HomeDataListener
import com.mtv.app.shopme.feature.contract.HomeEventListener
import com.mtv.app.shopme.feature.contract.HomeNavigationListener
import com.mtv.app.shopme.feature.contract.HomeStateListener
import com.mtv.app.shopme.feature.presentation.HomeViewModel
import com.mtv.app.shopme.feature.ui.HomeScreen

@Composable
fun HomeRoute(nav: NavController) {
    BaseRoute<HomeViewModel, HomeStateListener, HomeDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            HomeScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = homeEvent(vm),
                uiNavigation = homeNavigation(nav)
            )
        }
    }
}

private fun homeEvent(vm: HomeViewModel) = HomeEventListener(
    onDismissActiveDialog = {}
)

private fun homeNavigation(nav: NavController) = HomeNavigationListener(
    onBack = {}
)
