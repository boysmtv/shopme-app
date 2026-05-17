/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.43
 */

package com.mtv.app.shopme.nav.route.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.ProfileEffect
import com.mtv.app.shopme.feature.customer.contract.ProfileEvent
import com.mtv.app.shopme.feature.customer.presentation.ProfileViewModel
import com.mtv.app.shopme.feature.customer.ui.ProfileScreen
import com.mtv.app.shopme.nav.customer.CustomerNavActions

@Composable
fun ProfileRoute(nav: NavController) {

    val vm: ProfileViewModel = hiltViewModel()

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val baseUiState by vm.baseUiState.collectAsStateWithLifecycle()
    val refreshTick by nav.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("refreshTick", 0L)
        ?.collectAsStateWithLifecycle()
        ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableLongStateOf(0L) }

    LaunchedEffect(refreshTick) {
        if (refreshTick > 0L) vm.onEvent(ProfileEvent.Load)
    }

    BaseRoute(
        viewModel = vm,
        onLoad = ProfileEvent.Load,
        onEffect = { handleProfileEffect(nav, it) },
        onEvent = vm::onEvent,
        content = {
            BaseScreen(
                baseUiState = baseUiState,
                dismissDialog = { vm.onEvent(ProfileEvent.DismissDialog) }
            ) {
                ProfileScreen(
                    state = uiState,
                    event = vm::onEvent
                )
            }
        }
    )
}

private fun handleProfileEffect(
    nav: NavController,
    effect: ProfileEffect
) {
    when (effect) {
        ProfileEffect.NavigateToEditProfile -> CustomerNavActions.toEditProfile(nav)
        ProfileEffect.NavigateToOrderHistory -> CustomerNavActions.toOrderHistory(nav)
        ProfileEffect.NavigateToFavorites -> CustomerNavActions.toFavorites(nav)
        ProfileEffect.NavigateToSettings -> CustomerNavActions.toSettings(nav)
        ProfileEffect.NavigateToHelpCenter -> CustomerNavActions.toHelpCenter(nav)
        is ProfileEffect.NavigateToOrder -> CustomerNavActions.toOrder(nav, effect.filter)
        ProfileEffect.NavigateToTnc -> CustomerNavActions.toTnc(nav)
        ProfileEffect.NavigateToSeller -> CustomerNavActions.toSeller(nav)
        ProfileEffect.NavigateToLogin -> CustomerNavActions.toLogin(nav)
    }
}
