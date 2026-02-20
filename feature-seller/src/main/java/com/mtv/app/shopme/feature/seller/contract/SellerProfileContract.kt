/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerProfileContract.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 10.00
 */

package com.mtv.app.shopme.feature.seller.contract

class SellerProfileStateListener(
    var isLoading: Boolean = false
)

class SellerProfileDataListener(
    var sellerName: String = "",
    var email: String = "",
    var phone: String = "",
    var storeName: String = "",
    var storeAddress: String = "",
    var isOnline: Boolean = true
)

class SellerProfileEventListener(
    val onToggleOnline: () -> Unit,
    val onEditProfile: () -> Unit,
    val onLogout: () -> Unit
)

class SellerProfileNavigationListener(
    val navigateToEditProfile: () -> Unit = {},
    val navigateToStoreSettings: () -> Unit = {},
    val navigateToBankAccount: () -> Unit = {},
    val navigateToChangePassword: () -> Unit = {},
    val navigateToHelpCenter: () -> Unit = {}
)
