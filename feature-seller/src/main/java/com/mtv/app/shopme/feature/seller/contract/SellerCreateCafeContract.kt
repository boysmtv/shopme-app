/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerCreateCafeContract.kt
 *
 * Last modified by Dedy Wijaya on 14/03/26 14.10
 */

package com.mtv.app.shopme.feature.seller.contract

import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class SellerCreateCafeStateListener(
    val isLoading: Boolean = false,
    val step: Int = 1,
    val errorMessage: String? = null
)

data class SellerCreateCafeDataListener(
    val cafeName: String = EMPTY_STRING,
    val phone: String = EMPTY_STRING,
    val minOrder: String = EMPTY_STRING,
    val openHours: String = EMPTY_STRING,
    val closeHours: String = EMPTY_STRING,
    val description: String = EMPTY_STRING,
    val cafePhoto: String? = null,

    val latitude: Double? = null,
    val longitude: Double? = null,

    val village: String = EMPTY_STRING,
    val block: String = EMPTY_STRING,
    val number: String = EMPTY_STRING,
    val rt: String = EMPTY_STRING,
    val rw: String = EMPTY_STRING
)

data class SellerCreateCafeEventListener(

    val onCafeNameChange: (String) -> Unit = {},
    val onPhoneChange: (String) -> Unit = {},
    val onMinOrderChange: (String) -> Unit = {},
    val onOpenHoursChange: (String) -> Unit = {},
    val onDescriptionChange: (String) -> Unit = {},

    val onVillageChange: (String) -> Unit = {},
    val onBlockChange: (String) -> Unit = {},
    val onNumberChange: (String) -> Unit = {},
    val onRtChange: (String) -> Unit = {},
    val onRwChange: (String) -> Unit = {},

    val onUploadPhoto: () -> Unit = {},
    val onPickLocation: () -> Unit = {},

    val onNextStep: () -> Unit = {},
    val onCreateCafe: () -> Unit = {}
)

data class SellerCreateCafeNavigationListener(
    val navigateBack: () -> Unit = {},
    val navigateSuccess: () -> Unit = {}
)