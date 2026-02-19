package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.app.shopme.feature.seller.presentation.SellerProductListViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerProductListScreen
import com.mtv.app.shopme.nav.SellerDestinations

@Composable
fun SellerProductListRoute(nav: NavController) {
    BaseRoute<SellerProductListViewModel, SellerProductListStateListener, SellerProductListDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            SellerProductListScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = sellerProductListEvent(vm),
                uiNavigation = sellerProductListNavigation(nav)
            )
        }
    }
}

private fun sellerProductListEvent(vm: SellerProductListViewModel) =
    SellerProductListEventListener(
        onDeleteProduct = { vm.deleteProduct() }
    )

private fun sellerProductListNavigation(nav: NavController) =
    SellerProductListNavigationListener(
        onBack = {},
        onNavigateToAdd = {
            nav.navigate(SellerDestinations.PRODUCT_ADD)
        },
        onNavigateToEdit = {}
    )
