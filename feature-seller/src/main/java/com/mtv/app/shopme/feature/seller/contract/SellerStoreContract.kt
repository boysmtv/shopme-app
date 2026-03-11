/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerStoreContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 10.00
 */

package com.mtv.app.shopme.feature.seller.contract

class SellerStoreStateListener(
    var isLoading: Boolean = false
)

class SellerStoreDataListener(
    var sellerName: String = "",
    var email: String = "",
    var phone: String = "",
    var storeName: String = "",
    var storeAddress: String = "",
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
