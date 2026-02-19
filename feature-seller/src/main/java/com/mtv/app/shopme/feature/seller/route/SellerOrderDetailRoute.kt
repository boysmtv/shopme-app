/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerOrderDetailRoute.kt
 *
 * Last modified by Dedy Wijaya on 18/02/26 13.04
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailStateListener
import com.mtv.app.shopme.feature.seller.presentation.SellerOrderDetailViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerOrderDetailScreen

@Composable
fun SellerOrderDetailRoute(nav: NavController) {
    BaseRoute<SellerOrderDetailViewModel, SellerOrderDetailStateListener, SellerOrderDetailDataListener> { vm, base, uiState, uiData ->
        BaseScreen(
            baseUiState = base,
            onDismissError = vm::dismissError
        ) {
            SellerOrderDetailScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = sellerOrderDetailEvent(vm),
                uiNavigation = sellerOrderDetailNavigation(nav)
            )
        }
    }
}

private fun sellerOrderDetailEvent(vm: SellerOrderDetailViewModel) = SellerOrderDetailEventListener(
    onChangeStatus = { _ ->
    },
    onSaveStatus = {
    }
)

private fun sellerOrderDetailNavigation(nav: NavController) = SellerOrderDetailNavigationListener(
    onBack = {
        nav.popBackStack()
    }
)
