/*
 * Project: Boys.mtv@gmail.com
 * File: CafeCreateRoute.kt
 *
 * Last modified by Dedy Wijaya on 13/03/2026 13.42
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.CafeCreateEffect
import com.mtv.app.shopme.feature.seller.contract.CafeCreateEvent
import com.mtv.app.shopme.feature.seller.presentation.CafeCreateViewModel
import com.mtv.app.shopme.feature.seller.ui.CafeCreateScreen

@Composable
fun CafeCreateRoute(nav: NavController) {

    val vm: CafeCreateViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = CafeCreateEvent.Load,
        onEffect = { handleCafeCreateEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(CafeCreateEvent.DismissDialog) }
            ) {
                CafeCreateScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleCafeCreateEffect(
    nav: NavController,
    effect: CafeCreateEffect
) {
    when (effect) {
        CafeCreateEffect.NavigateBack -> nav.popBackStack()

        CafeCreateEffect.OpenImagePicker -> {
            // trigger launcher (ActivityResult)
        }

        CafeCreateEffect.CreateSuccess -> {
            nav.popBackStack()
        }
    }
}