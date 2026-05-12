/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.nav.route.auth

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.auth.contract.SplashEffect
import com.mtv.app.shopme.feature.auth.contract.SplashEvent
import com.mtv.app.shopme.feature.auth.presentation.SplashViewModel
import com.mtv.app.shopme.feature.auth.ui.SplashScreen
import com.mtv.app.shopme.nav.customer.CustomerNavActions

@Composable
fun SplashRoute(nav: NavController) {

    val vm: SplashViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()

    BaseRoute(
        viewModel = vm,
        onLoad = SplashEvent.Load,
        onEffect = { handleEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.dismissDialog() }
            ) {
                SplashScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleEffect(
    nav: NavController,
    effect: SplashEffect
) {
    when (effect) {
        SplashEffect.NavigateToHome -> CustomerNavActions.toHome(nav)
        SplashEffect.NavigateToLogin -> CustomerNavActions.toLogin(nav)
        SplashEffect.ExitApp -> (nav.context as? Activity)?.finishAffinity()
    }
}
