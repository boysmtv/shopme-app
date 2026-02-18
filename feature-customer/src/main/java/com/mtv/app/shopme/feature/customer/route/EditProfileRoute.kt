/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: EditProfileRoute.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.02
 */

package com.mtv.app.shopme.feature.customer.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.*
import com.mtv.app.shopme.feature.customer.presentation.EditProfileViewModel
import com.mtv.app.shopme.feature.customer.ui.EditProfileScreen

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
    onSaveClicked = { name, phone, email ->
        vm.onSaveProfile(name, phone, email)
    }
)

private fun editProfileNavigation(nav: NavController) = EditProfileNavigationListener(
    onBack = {
        nav.popBackStack()
    }
)
