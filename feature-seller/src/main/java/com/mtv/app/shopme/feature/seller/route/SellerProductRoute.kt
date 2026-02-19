/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProductRoute.kt
 *
 * Last modified by Dedy Wijaya on 19/02/26 09.00
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.presentation.SellerProductViewModel
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.app.shopme.feature.seller.ui.SellerProductListScreen
import com.mtv.app.shopme.feature.seller.ui.SellerProductFormScreen
import com.mtv.app.shopme.nav.SellerDestinations

@Composable
fun SellerProductRoute(nav: NavController, productId: String? = null) {
    BaseRoute<SellerProductViewModel, SellerProductStateListener, SellerProductDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            if (productId != null) {
                val product = uiState.productList.find { it.id == productId }
                SellerProductFormScreen(
                    uiData = uiData.copy(selectedProduct = product),
                    uiEvent = sellerProductEvent(vm),
                    onBack = { nav.popBackStack() }
                )
            } else {
                SellerProductListScreen(
                    uiState = uiState,
                    uiData = uiData,
                    uiEvent = sellerProductEvent(vm),
                    uiNavigation = sellerProductNavigation(nav)
                )
            }
        }
    }
}

private fun sellerProductEvent(vm: SellerProductViewModel) = SellerProductEventListener(
    onAddProduct = { vm.addProduct(it) },
    onUpdateProduct = { vm.updateProduct(it) },
    onDeleteProduct = { vm.deleteProduct(it) }
)

private fun sellerProductNavigation(nav: NavController) = SellerProductNavigationListener(
    onBack = { nav.popBackStack() },
    navigateToAddProduct = { nav.navigate(SellerDestinations.PRODUCT_ADD) },
    navigateToEditProduct = { product -> nav.navigate("seller_product_edit/${product.id}") }
)
