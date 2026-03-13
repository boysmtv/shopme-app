/*
 * Project: Boys.mtv@gmail.com
 * File: CafeCreateContract.kt
 *
 * Last modified by Dedy Wijaya on 13/03/2026 13.40
 */

package com.mtv.app.shopme.feature.seller.contract

data class CafeCreateStateListener(
    var isLoading: Boolean = false
)

data class CafeCreateDataListener(

    var name: String = "",
    var phone: String = "",
    var description: String = "",
    var minimalOrder: String = "",
    var openTime: String = "",
    var closeTime: String = "",
    var image: String? = null,
    var isActive: Boolean = true
)

data class CafeCreateEventListener(

    val onNameChange: (String) -> Unit,
    val onPhoneChange: (String) -> Unit,
    val onDescriptionChange: (String) -> Unit,
    val onMinimalOrderChange: (String) -> Unit,
    val onOpenTimeChange: (String) -> Unit,
    val onCloseTimeChange: (String) -> Unit,

    val onUploadImage: () -> Unit,
    val onCreateCafe: () -> Unit
)

data class CafeCreateNavigationListener(
    val navigateBack: () -> Unit = {}
)