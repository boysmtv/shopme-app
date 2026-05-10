/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ChatSupportRoute.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 11.13
 */

package com.mtv.app.shopme.nav.route.customer

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.ChatSupportEffect
import com.mtv.app.shopme.feature.customer.contract.ChatSupportEvent
import com.mtv.app.shopme.feature.customer.presentation.ChatSupportViewModel
import com.mtv.app.shopme.feature.customer.ui.ChatSupportScreen

@Composable
fun ChatSupportRoute(nav: NavController) {

    val vm: ChatSupportViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BaseRoute(
        viewModel = vm,
        onLoad = ChatSupportEvent.Load,
        onEffect = { handleChatSupportEffect(nav, context, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(ChatSupportEvent.DismissDialog) }
            ) {
                ChatSupportScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleChatSupportEffect(
    nav: NavController,
    context: Context,
    effect: ChatSupportEffect
) {
    when (effect) {
        ChatSupportEffect.NavigateBack -> nav.popBackStack()
        is ChatSupportEffect.OpenIntent -> context.startActivity(effect.intent)
    }
}
