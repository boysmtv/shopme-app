/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeTncContract.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 13.09
 */

package com.mtv.app.shopme.feature.seller.contract

import androidx.navigation.NavController

data class SellerCreateCafeTncStateListener(
    val isLoading: Boolean = false
)

data class SellerCreateCafeTncDataListener(
    val agreeTerms: Boolean = false,
    val agreeFoodSafety: Boolean = false,
    val agreeLocation: Boolean = false
)

data class SellerCreateCafeTncEventListener(

    val onAgreeTerms: (Boolean) -> Unit,
    val onAgreeFoodSafety: (Boolean) -> Unit,
    val onAgreeLocation: (Boolean) -> Unit,

    val onNext: () -> Unit
)

data class SellerCreateCafeTncNavigationListener(
    val navigateBack: () -> Unit = {},
    val navigateNext: (navController: NavController) -> Unit = {}

)