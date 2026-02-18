/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderRoute.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 16.30
 */

package com.mtv.app.shopme.feature.seller.route

/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderRoute.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 13.20
 */

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerStateListener
import com.mtv.app.shopme.feature.seller.presentation.SellerOrderViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerOrderScreen

@Composable
fun SellerOrderRoute(nav: NavController) {
    BaseRoute<SellerOrderViewModel, SellerStateListener, SellerDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            SellerOrderScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = sellerEvent(vm),
                uiNavigation = sellerNavigation(nav)
            )
        }
    }
}

private fun sellerEvent(vm: SellerOrderViewModel) = SellerEventListener(
    onRefresh = {
        // Tambahkan logic refresh order jika diperlukan
    }
)

private fun sellerNavigation(nav: NavController) = SellerNavigationListener(
    onNavigateToOrderDetail = { orderId ->
        nav.navigate("seller_order_detail/$orderId")
    }
)
