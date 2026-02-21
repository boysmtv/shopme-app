/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChangePinRoute.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.16
 */

package com.mtv.app.shopme.feature.auth.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.auth.contract.ChangePinDataListener
import com.mtv.app.shopme.feature.auth.contract.ChangePinEventListener
import com.mtv.app.shopme.feature.auth.contract.ChangePinNavigationListener
import com.mtv.app.shopme.feature.auth.contract.ChangePinStateListener
import com.mtv.app.shopme.feature.auth.presentation.ChangePinViewModel
import com.mtv.app.shopme.feature.auth.ui.ChangePinScreen

@Composable
fun ChangePinRoute(nav: NavController) {
    BaseRoute<ChangePinViewModel, ChangePinStateListener, ChangePinDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            ChangePinScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = changePinEvent(vm),
                uiNavigation = changePinNavigation(nav)
            )
        }
    }
}

private fun changePinEvent(vm: ChangePinViewModel) = ChangePinEventListener(
    onOldPinChange = vm::updateOldPin,
    onNewPinChange = vm::updateNewPin,
    onConfirmPinChange = vm::updateConfirmPin,
    onSubmit = vm::submit
)

private fun changePinNavigation(nav: NavController) = ChangePinNavigationListener(
    onBack = { nav.popBackStack() }
)