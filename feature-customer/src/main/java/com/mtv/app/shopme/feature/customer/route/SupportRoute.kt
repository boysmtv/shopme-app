/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SupportRoute.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 10.39
 */

package com.mtv.app.shopme.feature.customer.route

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.SupportDataListener
import com.mtv.app.shopme.feature.customer.contract.SupportEventListener
import com.mtv.app.shopme.feature.customer.contract.SupportNavigationListener
import com.mtv.app.shopme.feature.customer.contract.SupportStateListener
import com.mtv.app.shopme.feature.customer.presentation.SupportViewModel
import com.mtv.app.shopme.feature.customer.ui.SupportScreen
import com.mtv.app.shopme.nav.CustomerDestinations

@Composable
fun SupportRoute(nav: NavController) {
    BaseRoute<SupportViewModel, SupportStateListener, SupportDataListener> { vm, base, uiState, uiData ->
        val context = LocalContext.current
        val pm = context.packageManager

        LaunchedEffect(Unit) {
            vm.intentFlow.collect { intent ->
                context.startActivity(intent)
            }
        }

        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            SupportScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = contactEvent(vm, context, pm),
                uiNavigation = contactNavigation(nav)
            )
        }
    }
}

private fun contactEvent(
    vm: SupportViewModel,
    context: Context,
    pm: PackageManager
) = SupportEventListener(

    onOpenWhatsapp = {
        vm.openWhatsapp(vm.uiData.value.whatsapp, pm)
    },

    onOpenEmail = {
        vm.openEmailWithAttachment(context, pm)
    },

    onOpenDial = {
        vm.openDial(vm.uiData.value.phone, pm)
    }
)

private fun contactNavigation(nav: NavController) =
    SupportNavigationListener(
        onBack = { nav.popBackStack() },
        onLiveChat = { nav.navigate(CustomerDestinations.CHAT_SUPPORT_GRAPH) }
    )