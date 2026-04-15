/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeContract.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 14.10
 */

package com.mtv.app.shopme.feature.seller.contract

data class SellerCreateCafeUiState(
    val isLoading: Boolean = false,
    val step: Int = 1,

    val cafeName: String = "",
    val phone: String = "",
    val minOrder: String = "",
    val openHours: String = "",
    val closeHours: String = "",
    val description: String = "",
    val cafePhoto: String? = null,

    val latitude: Double? = null,
    val longitude: Double? = null,

    val village: String = "",
    val block: String = "",
    val number: String = "",
    val rt: String = "",
    val rw: String = ""
)

sealed class SellerCreateCafeEvent {

    object Load : SellerCreateCafeEvent()
    object DismissDialog : SellerCreateCafeEvent()

    data class ChangeCafeName(val value: String) : SellerCreateCafeEvent()
    data class ChangePhone(val value: String) : SellerCreateCafeEvent()
    data class ChangeMinOrder(val value: String) : SellerCreateCafeEvent()
    data class ChangeOpenHours(val value: String) : SellerCreateCafeEvent()
    data class ChangeCloseHours(val value: String) : SellerCreateCafeEvent()
    data class ChangeDescription(val value: String) : SellerCreateCafeEvent()

    data class ChangeVillage(val value: String) : SellerCreateCafeEvent()
    data class ChangeBlock(val value: String) : SellerCreateCafeEvent()
    data class ChangeNumber(val value: String) : SellerCreateCafeEvent()
    data class ChangeRt(val value: String) : SellerCreateCafeEvent()
    data class ChangeRw(val value: String) : SellerCreateCafeEvent()

    object UploadPhoto : SellerCreateCafeEvent()
    object PickLocation : SellerCreateCafeEvent()
    object PrevStep : SellerCreateCafeEvent()
    object NextStep : SellerCreateCafeEvent()
    object CreateCafe : SellerCreateCafeEvent()

    object ClickBack : SellerCreateCafeEvent()
    data class ImageSelected(val uri: String) : SellerCreateCafeEvent()
    object RemovePhoto : SellerCreateCafeEvent()
}

sealed class SellerCreateCafeEffect {
    object NavigateBack : SellerCreateCafeEffect()
    object NavigateSuccess : SellerCreateCafeEffect()

    object OpenImagePicker : SellerCreateCafeEffect()
    object OpenLocationPicker : SellerCreateCafeEffect()
}