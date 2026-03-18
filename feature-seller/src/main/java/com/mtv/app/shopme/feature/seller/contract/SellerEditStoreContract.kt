/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerEditStoreContract.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 15.49
 */

package com.mtv.app.shopme.feature.seller.contract

import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class SellerEditStoreStateListener(
    var isLoading: Boolean = false
)


data class SellerEditStoreDataListener(
    var storeName: String = EMPTY_STRING,
    var phone: String = EMPTY_STRING,
    var minOrder: String = EMPTY_STRING,
    var storeOpen: String = EMPTY_STRING,
    var description: String = EMPTY_STRING,
    var storePhoto: String? = null,
    var village: String = EMPTY_STRING,
    var block: String = EMPTY_STRING,
    var number: String = EMPTY_STRING,
    var rt: String = EMPTY_STRING,
    var rw: String = EMPTY_STRING
)

data class SellerEditStoreEventListener(
    val onStoreNameChange: (String) -> Unit,
    val onPhoneChange: (String) -> Unit,
    val onDescriptionChange: (String) -> Unit,

    val onVillageChange: (String) -> Unit,
    val onBlockChange: (String) -> Unit,
    val onNumberChange: (String) -> Unit,
    val onRtChange: (String) -> Unit,
    val onRwChange: (String) -> Unit,

    val onUploadPhoto: () -> Unit,
    val onSave: () -> Unit
)

data class SellerEditStoreNavigationListener(
    val navigateBack: () -> Unit = {}
)