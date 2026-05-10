/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ContactSupportViewModel.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 10.38
 */

package com.mtv.app.shopme.feature.customer.presentation

import android.content.Intent
import androidx.core.net.toUri
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.customer.contract.SupportEffect
import com.mtv.app.shopme.feature.customer.contract.SupportEvent
import com.mtv.app.shopme.feature.customer.contract.SupportUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class SupportViewModel @Inject constructor() :
    BaseEventViewModel<SupportEvent, SupportEffect>() {

    private val _state = MutableStateFlow(SupportUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SupportEvent) {
        when (event) {
            is SupportEvent.Load -> {}
            is SupportEvent.DismissDialog -> dismissDialog()

            is SupportEvent.OpenWhatsapp -> openWhatsapp()
            is SupportEvent.OpenEmail -> openEmail()
            is SupportEvent.OpenDial -> openDial()

            is SupportEvent.ClickBack -> emitEffect(SupportEffect.NavigateBack)
            is SupportEvent.ClickLiveChat -> emitEffect(SupportEffect.NavigateLiveChat)
        }
    }

    private fun openWhatsapp() {
        val number = _state.value.whatsapp
        val encodedMessage = URLEncoder.encode(
            "Halo tim Shopme, saya butuh bantuan.",
            StandardCharsets.UTF_8.toString()
        )
        val uri = "https://wa.me/$number?text=$encodedMessage".toUri()

        emitEffect(SupportEffect.OpenIntent(Intent(Intent.ACTION_VIEW, uri)))
    }

    private fun openDial() {
        val phone = _state.value.phone
        val intent = Intent(Intent.ACTION_DIAL, "tel:$phone".toUri())

        emitEffect(SupportEffect.OpenIntent(intent))
    }

    private fun openEmail() {
        val email = _state.value.email
        val subject = URLEncoder.encode("Support Request", StandardCharsets.UTF_8.toString())
        val body = URLEncoder.encode("Mohon bantuan terkait aplikasi Shopme.", StandardCharsets.UTF_8.toString())
        val uri = "mailto:$email?subject=$subject&body=$body".toUri()

        emitEffect(SupportEffect.OpenIntent(Intent(Intent.ACTION_SENDTO, uri)))
    }
}
