/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.nav.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.presentation.HomeViewModel
import com.mtv.app.shopme.feature.customer.ui.HomeScreen
import com.mtv.app.shopme.nav.customer.CustomerNavActions

@Composable
fun HomeRoute(nav: NavController) {

    val vm: HomeViewModel = hiltViewModel()

    val state by vm.state.collectAsStateWithLifecycle()
    val base by vm.baseUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.load()
    }

    BaseScreen(
        baseUiState = base,
        onDismissError = vm::dismissError
    ) {
        HomeScreen(
            state = state,
            onClickFood = { id ->
                CustomerNavActions.toDetail(nav, id)
            },
            onClickSearch = {
                CustomerNavActions.toSearch(nav)
            },
            onClickNotif = {
                CustomerNavActions.toNotif(nav)
            },
            onRefresh = {
                vm.refresh()
            }
        )
    }
}