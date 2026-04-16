/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChangePinRoute.kt
 *
 * Last modified by Dedy Wijaya on 21/02/26 23.16
 */

package com.mtv.app.shopme.nav.route.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.auth.contract.ChangePinEffect
import com.mtv.app.shopme.feature.auth.contract.ChangePinEvent
import com.mtv.app.shopme.feature.auth.presentation.ChangePinViewModel
import com.mtv.app.shopme.feature.auth.ui.ChangePinScreen

@Composable
fun ChangePinRoute(nav: NavController) {

    val vm: ChangePinViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(ChangePinEvent.DismissDialog) }
            ) {
                ChangePinScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleEffect(
    nav: NavController,
    effect: ChangePinEffect
) {
    when (effect) {
        ChangePinEffect.NavigateBack -> nav.popBackStack()
    }
}