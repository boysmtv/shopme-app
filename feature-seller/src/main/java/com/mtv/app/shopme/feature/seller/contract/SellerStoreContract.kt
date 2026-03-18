/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerStoreContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 10.00
 */

package com.mtv.app.shopme.feature.seller.contract

import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

class SellerStoreStateListener(
    var isLoading: Boolean = false
)

class SellerStoreDataListener(
    var sellerName: String = EMPTY_STRING,
    var email: String = EMPTY_STRING,
    var phone: String = EMPTY_STRING,
    var storeName: String = EMPTY_STRING,
    var storeAddress: String = EMPTY_STRING,
    var isOnline: Boolean = true
)

class SellerStoreEventListener(
    val onToggleOnline: () -> Unit,
    val onEditStore: () -> Unit,
    val onLogout: () -> Unit
)

class SellerStoreNavigationListener(
    val navigateToEditProfile: () -> Unit = {},
    val navigateToStoreSettings: () -> Unit = {},
    val navigateToBankAccount: () -> Unit = {},
    val navigateToChangePassword: () -> Unit = {},
    val navigateToHelpCenter: () -> Unit = {}
)
