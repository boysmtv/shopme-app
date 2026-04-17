/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerEditStoreContract.kt
 *
 * Last modified by Dedy Wijaya on 08/03/26 15.49
 */

package com.mtv.app.shopme.feature.seller.contract

data class SellerEditStoreUiState(
    val isLoading: Boolean = false,

    val selectedTab: Int = 0,

    val storeName: String = "",
    val phone: String = "",
    val minOrder: String = "",
    val storeOpen: String = "",
    val description: String = "",
    val storePhoto: String? = null,

    val village: String = "",
    val block: String = "",
    val number: String = "",
    val rt: String = "",
    val rw: String = ""
)

sealed class SellerEditStoreEvent {

    object Load : SellerEditStoreEvent()
    object DismissDialog : SellerEditStoreEvent()

    // BASIC
    data class ChangeStoreName(val value: String) : SellerEditStoreEvent()
    data class ChangePhone(val value: String) : SellerEditStoreEvent()
    data class ChangeMinOrder(val value: String) : SellerEditStoreEvent()
    data class ChangeStoreOpen(val value: String) : SellerEditStoreEvent()
    data class ChangeDescription(val value: String) : SellerEditStoreEvent()

    // ADDRESS
    data class ChangeVillage(val value: String) : SellerEditStoreEvent()
    data class ChangeBlock(val value: String) : SellerEditStoreEvent()
    data class ChangeNumber(val value: String) : SellerEditStoreEvent()
    data class ChangeRt(val value: String) : SellerEditStoreEvent()
    data class ChangeRw(val value: String) : SellerEditStoreEvent()

    // UI STATE
    data class ChangeTab(val index: Int) : SellerEditStoreEvent()

    // ACTION
    object UploadPhoto : SellerEditStoreEvent()
    object RemovePhoto : SellerEditStoreEvent()
    data class PhotoSelected(val uri: String) : SellerEditStoreEvent()

    object Save : SellerEditStoreEvent()
    object ClickBack : SellerEditStoreEvent()
}

sealed class SellerEditStoreEffect {
    object NavigateBack : SellerEditStoreEffect()
    object OpenImagePicker : SellerEditStoreEffect()
    object SaveSuccess : SellerEditStoreEffect()
}