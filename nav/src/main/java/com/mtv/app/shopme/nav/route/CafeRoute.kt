/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeRoute.kt
 *
 * Last modified by Dedy Wijaya on 14/02/26 22.34
 */

package com.mtv.app.shopme.nav.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.CafeEffect
import com.mtv.app.shopme.feature.customer.contract.CafeEvent
import com.mtv.app.shopme.feature.customer.presentation.CafeViewModel
import com.mtv.app.shopme.feature.customer.ui.CafeScreen
import com.mtv.app.shopme.nav.customer.CustomerNavActions

@Composable
fun CafeRoute(nav: NavController) {

    val vm: CafeViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = CafeEvent.Load,
        onEffect = { handleCafeEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(CafeEvent.DismissDialog) }
            ) {
                CafeScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleCafeEffect(
    nav: NavController,
    effect: CafeEffect
) {
    when (effect) {
        is CafeEffect.NavigateBack -> nav.popBackStack()
        is CafeEffect.NavigateToChat -> CustomerNavActions.toChat(nav)
        is CafeEffect.NavigateToWhatsapp -> CustomerNavActions.toChat(nav)
        is CafeEffect.NavigateToDetail -> CustomerNavActions.toDetail(nav, effect.id)
    }
}