/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HelpRoute.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 10.26
 */

package com.mtv.app.shopme.nav.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.HelpDataListener
import com.mtv.app.shopme.feature.customer.contract.HelpEventListener
import com.mtv.app.shopme.feature.customer.contract.HelpNavigationListener
import com.mtv.app.shopme.feature.customer.contract.HelpStateListener
import com.mtv.app.shopme.feature.customer.presentation.HelpViewModel
import com.mtv.app.shopme.feature.customer.ui.HelpScreen
import com.mtv.app.shopme.nav.customer.CustomerDestinations

@Composable
fun HelpRoute(nav: NavController) {
    BaseRoute<HelpViewModel, HelpStateListener, HelpDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, dismissDialog = vm::dismissError) {
            HelpScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = helpEvent(vm),
                uiNavigation = helpNavigation(nav)
            )
        }
    }
}

private fun helpEvent(vm: HelpViewModel) =
    HelpEventListener(
        onRefresh = {},
        onToggleFaq = vm::onToggleFaq
    )

private fun helpNavigation(nav: NavController) =
    HelpNavigationListener(
        onBack = { nav.popBackStack() },
        onAbout = { nav.navigate("about_graph") },
        onPrivacy = { nav.navigate("privacy_graph") },
        onShipping = { nav.navigate("shipping_help_graph") },
        onPayment = { nav.navigate("payment_help_graph") },
        onContact = { nav.navigate(CustomerDestinations.SUPPORT_GRAPH) }
    )