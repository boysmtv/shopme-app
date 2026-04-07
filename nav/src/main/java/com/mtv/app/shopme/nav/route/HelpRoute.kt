/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HelpRoute.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 10.26
 */

package com.mtv.app.shopme.nav.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.common.navbar.customer.CustomerDestinations
import com.mtv.app.shopme.feature.customer.contract.HelpEffect
import com.mtv.app.shopme.feature.customer.contract.HelpEvent
import com.mtv.app.shopme.feature.customer.presentation.HelpViewModel
import com.mtv.app.shopme.feature.customer.ui.HelpScreen

@Composable
fun HelpRoute(nav: NavController) {

    val vm: HelpViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = HelpEvent.Load,
        onEffect = { handleHelpEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(HelpEvent.DismissDialog) }
            ) {
                HelpScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleHelpEffect(
    nav: NavController,
    effect: HelpEffect
) {
    when (effect) {
        HelpEffect.NavigateBack -> nav.popBackStack()
        HelpEffect.NavigateAbout -> nav.navigate("about_graph")
        HelpEffect.NavigatePrivacy -> nav.navigate("privacy_graph")
        HelpEffect.NavigateShipping -> nav.navigate("shipping_help_graph")
        HelpEffect.NavigatePayment -> nav.navigate("payment_help_graph")
        HelpEffect.NavigateContact -> nav.navigate(CustomerDestinations.SUPPORT_GRAPH)
    }
}