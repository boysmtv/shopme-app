/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductFormRoute.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 13.34
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.app.shopme.feature.seller.presentation.SellerProductFormViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerProductFormScreen

@Composable
fun SellerProductFormRoute(nav: NavController) {
    BaseRoute<SellerProductFormViewModel, SellerProductFormStateListener, SellerProductFormDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            SellerProductFormScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = sellerProductFormEvent(vm, nav),
                uiNavigation = sellerProductFormNavigation(nav)
            )
        }
    }
}

private fun sellerProductFormEvent(
    vm: SellerProductFormViewModel,
    nav: NavController
) = SellerProductFormEventListener(
    onSaveProduct = {
        vm.saveProduct(it)
        nav.popBackStack()
    },
    onDeleteProduct = {
        vm.deleteProduct(it)
        nav.popBackStack()
    }
)

private fun sellerProductFormNavigation(nav: NavController) =
    SellerProductFormNavigationListener(
        onBack = { nav.popBackStack() }
    )
