/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerEditStoreContract.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 15.49
 */

package com.mtv.app.shopme.feature.seller.contract

data class SellerEditStoreStateListener(
    var isLoading: Boolean = false
)


data class SellerEditStoreDataListener(
    var storeName: String = "",
    var phone: String = "",
    var minOrder: String = "",
    var storeOpen: String = "",
    var description: String = "",
    var storePhoto: String? = null,
    var village: String = "",
    var block: String = "",
    var number: String = "",
    var rt: String = "",
    var rw: String = ""
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