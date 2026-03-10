/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerEditStoreRoute.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 15.34
 */

package com.mtv.app.shopme.feature.seller.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mtv.app.shopme.common.base.BaseRoute
import com.mtv.app.shopme.common.base.BaseScreen
import com.mtv.app.shopme.feature.seller.contract.*
import com.mtv.app.shopme.feature.seller.presentation.SellerEditStoreViewModel
import com.mtv.app.shopme.feature.seller.ui.SellerEditStoreScreen

@Composable
fun SellerEditStoreRoute(nav: NavController) {
    BaseRoute<SellerEditStoreViewModel, SellerEditStoreStateListener, SellerEditStoreDataListener> { vm, base, uiState, uiData ->
        BaseScreen(baseUiState = base, onDismissError = vm::dismissError) {
            SellerEditStoreScreen(
                uiState = uiState,
                uiData = uiData,
                uiEvent = sellerEditStoreEvent(vm),
                uiNavigation = sellerEditStoreNavigation(nav)
            )
        }
    }
}

private fun sellerEditStoreEvent(vm: SellerEditStoreViewModel) =
    SellerEditStoreEventListener(
        onStoreNameChange = vm::onStoreNameChange,
        onPhoneChange = vm::onPhoneChange,
        onDescriptionChange = vm::onDescriptionChange,

        onVillageChange = vm::onVillageChange,
        onBlockChange = vm::onBlockChange,
        onNumberChange = vm::onNumberChange,
        onRtChange = vm::onRtChange,
        onRwChange = vm::onRwChange,

        onUploadPhoto = vm::onUploadPhoto,
        onSave = vm::saveStore
    )

private fun sellerEditStoreNavigation(nav: NavController) =
    SellerEditStoreNavigationListener(
        navigateBack = { nav.popBackStack() }
    )