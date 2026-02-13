/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: EditAddressRoute.kt
 *
 * Last modified by Dedy Wijaya on 13/02/26 11.06
 */

package com.mtv.app.shopme.feature.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.contract.EditAddressDataListener
import com.mtv.app.shopme.feature.contract.EditAddressEventListener
import com.mtv.app.shopme.feature.contract.EditAddressNavigationListener
import com.mtv.app.shopme.feature.contract.EditAddressStateListener
import com.mtv.app.shopme.feature.presentation.EditAddressViewModel
import com.mtv.app.shopme.feature.ui.EditAddressScreen

@Composable
fun EditAddressRoute(nav: NavController) {
    BaseRoute<EditAddressViewModel, EditAddressStateListener, EditAddressDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            EditAddressScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = editAddressEvent(vm),
                uiNavigation = editAddressNavigation(nav)
            )
        }
    }
}

private fun editAddressEvent(vm: EditAddressViewModel) = EditAddressEventListener(
    onSaveClicked = { village, block, number, rt, rw, map ->
        vm.onSaveProfile(village, block, number)
    }
)

private fun editAddressNavigation(nav: NavController) = EditAddressNavigationListener(
    onBack = {
        nav.popBackStack()
    }
)
