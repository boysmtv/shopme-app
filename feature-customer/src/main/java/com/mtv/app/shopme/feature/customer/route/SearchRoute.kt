/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchRoute.kt
 *
 * Last modified by Dedy Wijaya on 11/02/26 13.42
 */

package com.mtv.app.shopme.feature.customer.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.customer.contract.SearchDataListener
import com.mtv.app.shopme.feature.customer.contract.SearchEventListener
import com.mtv.app.shopme.feature.customer.contract.SearchNavigationListener
import com.mtv.app.shopme.feature.customer.contract.SearchStateListener
import com.mtv.app.shopme.feature.customer.presentation.SearchViewModel
import com.mtv.app.shopme.feature.customer.ui.SearchScreen
import com.mtv.app.shopme.nav.CustomerDestinations

@Composable
fun SearchRoute(nav: NavController) {
    BaseRoute<SearchViewModel, SearchStateListener, SearchDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            SearchScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = searchEvent(vm),
                uiNavigation = searchNavigation(nav)
            )
        }
    }
}

private fun searchEvent(vm: SearchViewModel) = SearchEventListener(
    onQueryChanged = { }
)

private fun searchNavigation(nav: NavController) = SearchNavigationListener(
    onBack = { nav.popBackStack() },
    onDetailClick = {
        nav.navigate(CustomerDestinations.DETAIL_GRAPH)
    }
)
