/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: EditProfileRoute.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.02
 */

package com.mtv.app.shopme.feature.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.contract.*
import com.mtv.app.shopme.feature.presentation.EditProfileViewModel
import com.mtv.app.shopme.feature.ui.EditProfileScreen

@Composable
fun EditProfileRoute(nav: NavController) {
    BaseRoute<EditProfileViewModel, EditProfileStateListener, EditProfileDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            EditProfileScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = editProfileEvent(vm),
                uiNavigation = editProfileNavigation(nav)
            )
        }
    }
}

private fun editProfileEvent(vm: EditProfileViewModel) = EditProfileEventListener(
    onSaveClicked = { name, phone, address ->
        vm.onSaveProfile(name, phone, address)
    }
)

private fun editProfileNavigation(nav: NavController) = EditProfileNavigationListener(
    onBack = {
        nav.popBackStack()
    }
)
