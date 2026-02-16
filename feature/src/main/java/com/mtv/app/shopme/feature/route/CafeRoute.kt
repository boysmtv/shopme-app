/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: CafeRoute.kt
 *
 * Last modified by Dedy Wijaya on 14/02/26 22.34
 */

package com.mtv.app.shopme.feature.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.contract.*
import com.mtv.app.shopme.feature.presentation.CafeViewModel
import com.mtv.app.shopme.feature.ui.CafeScreen

@Composable
fun CafeRoute(nav: NavController) {
    BaseRoute<CafeViewModel, CafeStateListener, CafeDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            CafeScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = cafeEvent(vm, nav),
                uiNavigation = cafeNavigation(nav)
            )
        }
    }
}

private fun cafeEvent(
    vm: CafeViewModel,
    nav: NavController
) = CafeEventListener(
    onFoodClick = { food ->
        nav.navigate("detail/${food.id}")
    }
)

private fun cafeNavigation(nav: NavController) = CafeNavigationListener(
    onBack = { nav.popBackStack() }
)
