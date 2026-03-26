/*
 * Project: Boys.mtv@gmail.com
 * File: CafeCreateRoute.kt
 *
 * Last modified by Dedy Wijaya on 13/03/2026 13.42
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.CafeCreateDataListener
import com.mtv.app.shopme.feature.seller.contract.CafeCreateEventListener
import com.mtv.app.shopme.feature.seller.contract.CafeCreateNavigationListener
import com.mtv.app.shopme.feature.seller.contract.CafeCreateStateListener
import com.mtv.app.shopme.feature.seller.presentation.CafeCreateViewModel
import com.mtv.app.shopme.feature.seller.ui.CafeCreateScreen

@Composable
fun CafeCreateRoute(nav: NavController) {
    BaseRoute<CafeCreateViewModel, CafeCreateStateListener, CafeCreateDataListener> { vm, base, uiState, uiData ->
        BaseScreen(
            baseUiState = base,
            dismissDialog = vm::dismissError
        ) {

            CafeCreateScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = cafeCreateEvent(vm),
                uiNavigation = cafeCreateNavigation(nav)
            )
        }
    }
}

private fun cafeCreateEvent(vm: CafeCreateViewModel) =
    CafeCreateEventListener(
        onNameChange = vm::onNameChange,
        onPhoneChange = vm::onPhoneChange,
        onDescriptionChange = vm::onDescriptionChange,
        onMinimalOrderChange = vm::onMinimalOrderChange,
        onOpenTimeChange = vm::onOpenTimeChange,
        onCloseTimeChange = vm::onCloseTimeChange,
        onUploadImage = vm::onUploadImage,
        onCreateCafe = vm::createCafe
    )

private fun cafeCreateNavigation(nav: NavController) =
    CafeCreateNavigationListener(
        navigateBack = { nav.popBackStack() }
    )