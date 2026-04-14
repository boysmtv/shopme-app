/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: EditProfileRoute.kt
 *
 * Last modified by Dedy Wijaya on 12/02/26 14.02
 */

package com.mtv.app.shopme.nav.route.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.EditProfileEffect
import com.mtv.app.shopme.feature.customer.contract.EditProfileEvent
import com.mtv.app.shopme.feature.customer.presentation.EditProfileViewModel
import com.mtv.app.shopme.feature.customer.ui.EditProfileScreen

@Composable
fun EditProfileRoute(nav: NavController) {

    val vm: EditProfileViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = EditProfileEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(EditProfileEvent.DismissDialog) }
            ) {
                EditProfileScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleEffect(
    nav: NavController,
    effect: EditProfileEffect
) {
    when (effect) {
        EditProfileEffect.NavigateBack -> nav.popBackStack()
    }
}