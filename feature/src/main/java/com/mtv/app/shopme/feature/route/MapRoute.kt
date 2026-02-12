/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: MapRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.feature.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.contract.MapDataListener
import com.mtv.app.shopme.feature.contract.MapEventListener
import com.mtv.app.shopme.feature.contract.MapNavigationListener
import com.mtv.app.shopme.feature.contract.MapStateListener
import com.mtv.app.shopme.feature.presentation.MapViewModel

@Composable
fun MapRoute(nav: NavController) {
    BaseRoute<MapViewModel, MapStateListener, MapDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
//            MapScreen(
//                uiState = uiState,
//                uiData = uiData,
//                uiEvent = mapEvent(vm),
//                uiNavigation = mapNavigation(nav)
//            )
        }
    }
}

private fun mapEvent(vm: MapViewModel) = MapEventListener(
    onDismissActiveDialog = { }
)

private fun mapNavigation(nav: NavController) = MapNavigationListener(
    onBack = { nav.popBackStack() }
)
