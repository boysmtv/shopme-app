/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeRoute.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 14.12
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeDataListener
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeEventListener
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeNavigationListener
import com.mtv.app.shopme.feature.seller.contract.SellerCreateCafeStateListener
import com.mtv.app.shopme.feature.seller.presentation.SellerCreateCafeViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerCreateCafeScreen
import com.mtv.app.shopme.nav.seller.SellerDestinations

@Composable
fun SellerCreateCafeRoute(nav: NavController) {
    BaseRoute<SellerCreateCafeViewModel, SellerCreateCafeStateListener, SellerCreateCafeDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, dismissDialog = vm::dismissError) {
            SellerCreateCafeScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = sellerCreateCafeEvent(vm),
                uiNavigation = sellerCreateCafeNavigation(nav)
            )
        }
    }
}

private fun sellerCreateCafeEvent(
    vm: SellerCreateCafeViewModel
) = SellerCreateCafeEventListener(
    onCafeNameChange = vm::onCafeNameChange,
    onPhoneChange = vm::onPhoneChange,
    onMinOrderChange = vm::onMinOrderChange,
    onOpenHoursChange = vm::onOpenHoursChange,
    onDescriptionChange = vm::onDescriptionChange,

    onVillageChange = vm::onVillageChange,
    onBlockChange = vm::onBlockChange,
    onNumberChange = vm::onNumberChange,
    onRtChange = vm::onRtChange,
    onRwChange = vm::onRwChange,

    onUploadPhoto = vm::onUploadPhoto,
    onPickLocation = vm::onPickLocation,

    onNextStep = vm::onNextStep,
    onCreateCafe = vm::createCafe
)

private fun sellerCreateCafeNavigation(
    nav: NavController
) = SellerCreateCafeNavigationListener(
    navigateBack = { nav.popBackStack() },
    navigateSuccess = {
        nav.navigate(SellerDestinations.SELLER_GRAPH) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }
)