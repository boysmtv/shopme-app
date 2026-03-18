/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeTncRoute.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 13.10
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.app.shopme.feature.seller.presentation.SellerCreateCafeTncViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerCreateCafeTncScreen
import com.mtv.app.shopme.nav.SellerDestinations

@Composable
fun SellerCreateCafeTncRoute(nav: NavController) {
    BaseRoute<SellerCreateCafeTncViewModel, SellerCreateCafeTncStateListener, SellerCreateCafeTncDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            SellerCreateCafeTncScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = sellerCreateCafeTncEvent(vm),
                uiNavigation = sellerCreateCafeTncNavigation(nav)
            )
        }
    }
}

private fun sellerCreateCafeTncEvent(vm: SellerCreateCafeTncViewModel) =
    SellerCreateCafeTncEventListener(
        onAgreeTerms = vm::onAgreeTerms,
        onAgreeFoodSafety = vm::onAgreeFoodSafety,
        onAgreeLocation = vm::onAgreeLocation,
        onNext = vm::next
    )

private fun sellerCreateCafeTncNavigation(nav: NavController) =
    SellerCreateCafeTncNavigationListener(
        navigateBack = { nav.popBackStack() },
        navigateNext = { nav.navigate(SellerDestinations.SELLER_CREATE_GRAPH) },
    )