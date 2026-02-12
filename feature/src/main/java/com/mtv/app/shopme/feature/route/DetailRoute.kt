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
import com.mtv.app.shopme.feature.contract.DetailDataListener
import com.mtv.app.shopme.feature.contract.DetailEventListener
import com.mtv.app.shopme.feature.contract.DetailNavigationListener
import com.mtv.app.shopme.feature.contract.DetailStateListener
import com.mtv.app.shopme.feature.presentation.DetailViewModel
import com.mtv.app.shopme.feature.ui.DetailScreen

@Composable
fun DetailRoute(nav: NavController) {
    BaseRoute<DetailViewModel, DetailStateListener, DetailDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            DetailScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = detailEvent(vm),
                uiNavigation = detailNavigation(nav)
            )
        }
    }
}

private fun detailEvent(vm: DetailViewModel) = DetailEventListener(
    onDismissActiveDialog = {}
)

private fun detailNavigation(nav: NavController) = DetailNavigationListener(
    onBack = {}
)
