/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ResetRoute.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.54
 */

package com.mtv.app.shopme.nav.route.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.auth.contract.ResetEffect
import com.mtv.app.shopme.feature.auth.contract.ResetEvent
import com.mtv.app.shopme.feature.auth.presentation.ResetViewModel
import com.mtv.app.shopme.feature.auth.ui.ResetScreen

@Composable
fun ResetRoute(nav: NavController) {

    val vm: ResetViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(ResetEvent.DismissDialog) }
            ) {
                ResetScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleEffect(
    nav: NavController,
    effect: ResetEffect
) {
    when (effect) {
        ResetEffect.NavigateBack -> nav.popBackStack()
    }
}