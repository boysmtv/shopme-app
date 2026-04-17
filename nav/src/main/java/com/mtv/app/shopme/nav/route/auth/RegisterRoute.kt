/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: RegisterRoute.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.53
 */

package com.mtv.app.shopme.nav.route.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.auth.contract.RegisterEffect
import com.mtv.app.shopme.feature.auth.contract.RegisterEvent
import com.mtv.app.shopme.feature.auth.presentation.RegisterViewModel
import com.mtv.app.shopme.feature.auth.ui.RegisterScreen

@Composable
fun RegisterRoute(nav: NavController) {

    val vm: RegisterViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(RegisterEvent.DismissDialog) }
            ) {
                RegisterScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleEffect(
    nav: NavController,
    effect: RegisterEffect
) {
    when (effect) {
        RegisterEffect.NavigateToLogin -> nav.popBackStack()
    }
}