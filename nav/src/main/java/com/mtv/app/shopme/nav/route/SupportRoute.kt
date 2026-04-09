/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SupportRoute.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 10.39
 */

package com.mtv.app.shopme.nav.route

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.common.navbar.customer.CustomerDestinations
import com.mtv.app.shopme.feature.customer.contract.SupportEffect
import com.mtv.app.shopme.feature.customer.contract.SupportEvent
import com.mtv.app.shopme.feature.customer.presentation.SupportViewModel
import com.mtv.app.shopme.feature.customer.ui.SupportScreen

@Composable
fun SupportRoute(nav: NavController) {

    val vm: SupportViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val pm = context.packageManager

    BaseRoute(
        viewModel = vm,
        onLoad = SupportEvent.Load,
        onEffect = { handleSupportEffect(nav, context, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(SupportEvent.DismissDialog) }
            ) {
                SupportScreen(
                    state = uiState,
                    event = { event ->
                        when (event) {
                            SupportEvent.OpenWhatsapp -> vm.onEvent(pm)
                            SupportEvent.OpenEmail -> vm.onEvent(context, pm)
                            SupportEvent.OpenDial -> vm.onEvent()
                            else -> vm.onEvent(event)
                        }
                    }
                )
            }
        }
    )
}

private fun handleSupportEffect(
    nav: NavController,
    context: Context,
    effect: SupportEffect
) {
    when (effect) {
        SupportEffect.NavigateBack -> nav.popBackStack()
        SupportEffect.NavigateLiveChat -> nav.navigate(CustomerDestinations.CHAT_SUPPORT_GRAPH)
        is SupportEffect.OpenIntent -> context.startActivity(effect.intent)
    }
}