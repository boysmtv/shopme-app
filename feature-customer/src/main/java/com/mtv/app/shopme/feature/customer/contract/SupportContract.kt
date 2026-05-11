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
    val phone: String = "",
    val email: String = "",
    val whatsapp: String = "",
    val whatsappMessageTemplate: String = "",
    val emailSubject: String = "",
    val emailBodyTemplate: String = "",
    val operationalHoursLabel: String = "",
    val statusLabel: String = "",
    val isOnline: Boolean = false
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
