/*
 * Project: Boys.mtv@gmail.com
 * File: CafeCreateContract.kt
 *
 * Last modified by Dedy Wijaya on 13/03/2026 13.40
 */

package com.mtv.app.shopme.feature.seller.contract

data class CafeCreateUiState(
    val isLoading: Boolean = false,

    val name: String = "",
    val phone: String = "",
    val description: String = "",
    val minimalOrder: String = "",
    val openTime: String = "",
    val closeTime: String = "",
    val image: String? = null,
    val isActive: Boolean = true
)

sealed class CafeCreateEvent {
    object Load : CafeCreateEvent()
    object DismissDialog : CafeCreateEvent()

    data class ChangeName(val value: String) : CafeCreateEvent()
    data class ChangePhone(val value: String) : CafeCreateEvent()
    data class ChangeDescription(val value: String) : CafeCreateEvent()
    data class ChangeMinimalOrder(val value: String) : CafeCreateEvent()
    data class ChangeOpenTime(val value: String) : CafeCreateEvent()
    data class ChangeCloseTime(val value: String) : CafeCreateEvent()

    object UploadImage : CafeCreateEvent()
    object Submit : CafeCreateEvent()

    object ClickBack : CafeCreateEvent()
}

sealed class CafeCreateEffect {
    object NavigateBack : CafeCreateEffect()
    object OpenImagePicker : CafeCreateEffect()

    object CreateSuccess : CafeCreateEffect()
}