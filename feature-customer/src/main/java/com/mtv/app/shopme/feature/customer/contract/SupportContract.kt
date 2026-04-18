/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SupportContract.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 10.38
 */

package com.mtv.app.shopme.feature.customer.contract

import android.content.Intent
import android.content.pm.PackageManager

data class SupportUiState(
    val isLoading: Boolean = false,
    val phone: String = "081234567890",
    val email: String = "support@shopme.com",
    val whatsapp: String = "6281234567890"
)

sealed class SupportEvent {
    object Load : SupportEvent()
    object DismissDialog : SupportEvent()
    object OpenWhatsapp : SupportEvent()
    object OpenEmail : SupportEvent()
    object OpenDial : SupportEvent()

    object ClickBack : SupportEvent()
    object ClickLiveChat : SupportEvent()
}

sealed class SupportEffect {
    object NavigateBack : SupportEffect()
    object NavigateLiveChat : SupportEffect()

    data class OpenIntent(val intent: Intent) : SupportEffect()
}