/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerPaymentMethodRoute.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 22.20
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodStateListener
import com.mtv.app.shopme.feature.seller.presentation.SellerPaymentMethodViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerPaymentMethodScreen

@Composable
fun SellerPaymentMethodRoute(nav: NavController) {

    BaseRoute<
            SellerPaymentMethodViewModel,
            SellerPaymentMethodStateListener,
            SellerPaymentMethodDataListener> { vm, base, uiState, uiData ->

        BaseScreen(
            baseUiState = base,
            dismissDialog = vm::dismissError
        ) {

            SellerPaymentMethodScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = SellerPaymentMethodEventListener(
                    onCashToggle = vm::toggleCash,
                    onBankChange = vm::onBankChange,
                    onGopayChange = vm::onGopayChange,
                    onDanaChange = vm::onDanaChange,
                    onOvoChange = vm::onOvoChange,
                    onSave = vm::save
                ),
                uiNavigation = SellerPaymentMethodNavigation(nav)
            )
        }
    }
}

private fun SellerPaymentMethodNavigation(nav: NavController) =
    SellerPaymentMethodNavigationListener(
        navigateBack = { nav.popBackStack() }
    )