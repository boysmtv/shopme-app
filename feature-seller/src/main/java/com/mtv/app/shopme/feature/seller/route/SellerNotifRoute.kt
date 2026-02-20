/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerNotifRoute.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.27
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerNotifData
import com.mtv.app.shopme.feature.seller.contract.SellerNotifEvent
import com.mtv.app.shopme.feature.seller.contract.SellerNotifNavigation
import com.mtv.app.shopme.feature.seller.contract.SellerNotifState
import com.mtv.app.shopme.feature.seller.presentation.SellerNotifViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerNotificationScreen

@Composable
fun SellerNotifRoute(nav: NavController) {
    BaseRoute<SellerNotifViewModel, SellerNotifState, SellerNotifData> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            SellerNotificationScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = SellerNotifEvent(
                    onNotificationClicked = {},
                    onGetNotification = vm::getLocalNotification,
                    onClearNotification = vm::clearNotification,
                    onDismissActiveDialog = vm::dismissDialog
                ),
                uiNavigation = SellerNotifNavigation(
                    onBack = { nav.popBackStack() }
                )
            )
        }
    }
}