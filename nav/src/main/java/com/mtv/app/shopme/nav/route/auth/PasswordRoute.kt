/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChangePasswordRoute.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 09.25
 */

package com.mtv.app.shopme.nav.route.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.auth.contract.PasswordEffect
import com.mtv.app.shopme.feature.auth.contract.PasswordEvent
import com.mtv.app.shopme.feature.auth.presentation.PasswordViewModel
import com.mtv.app.shopme.feature.auth.ui.PasswordScreen

@Composable
fun PasswordRoute(nav: NavController) {

    val vm: PasswordViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(PasswordEvent.DismissDialog) }
            ) {
                PasswordScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleEffect(
    nav: NavController,
    effect: PasswordEffect
) {
    when (effect) {
        PasswordEffect.NavigateBack -> nav.popBackStack()
    }
}