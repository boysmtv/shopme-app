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
import com.mtv.app.shopme.domain.model.SupportCenter
import com.mtv.app.shopme.domain.usecase.GetSupportCenterUseCase
import com.mtv.app.shopme.feature.customer.contract.SupportEffect
import com.mtv.app.shopme.feature.customer.contract.SupportEvent
import com.mtv.app.shopme.feature.customer.contract.SupportUiState
import com.mtv.based.core.network.utils.ErrorMessages
import com.mtv.based.core.network.utils.LoadState
import com.mtv.based.core.network.utils.UiError
import com.mtv.based.core.provider.utils.SessionManager
import com.mtv.based.core.provider.utils.dialog.UiDialog
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogStateV1
import com.mtv.based.uicomponent.core.component.dialog.dialogv1.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val getSupportCenterUseCase: GetSupportCenterUseCase,
    private val sessionManager: SessionManager
) :
    BaseEventViewModel<SupportEvent, SupportEffect>() {

    private val _state = MutableStateFlow(SupportUiState())
    val uiState = _state.asStateFlow()

    override fun onEvent(event: SupportEvent) {
        when (event) {
            is SupportEvent.Load -> load()
            is SupportEvent.DismissDialog -> dismissDialog()

            is SupportEvent.OpenWhatsapp -> openWhatsapp()
            is SupportEvent.OpenEmail -> openEmail()
            is SupportEvent.OpenDial -> openDial()

            is SupportEvent.ClickBack -> emitEffect(SupportEffect.NavigateBack)
            is SupportEvent.ClickLiveChat -> emitEffect(SupportEffect.NavigateLiveChat)
        }
    }

    private fun load() {
        observeDataFlow(
            flow = getSupportCenterUseCase(),
            onState = { state ->
                _state.update { current ->
                    when (state) {
                        is LoadState.Success -> state.data.toUiState()
                        else -> current.copy(isLoading = state is LoadState.Loading)
                    }
                }
            },
            onError = ::showError
        )
    }

    private fun openWhatsapp() {
        val number = _state.value.whatsapp
        if (number.isBlank()) return
        val encodedMessage = URLEncoder.encode(
            _state.value.whatsappMessageTemplate,
            StandardCharsets.UTF_8.toString()
        )
        val uri = "https://wa.me/$number?text=$encodedMessage".toUri()

        emitEffect(SupportEffect.OpenIntent(Intent(Intent.ACTION_VIEW, uri)))
    }

    private fun openDial() {
        val phone = _state.value.phone
        if (phone.isBlank()) return
        val intent = Intent(Intent.ACTION_DIAL, "tel:$phone".toUri())

        emitEffect(SupportEffect.OpenIntent(intent))
    }

    private fun openEmail() {
        val email = _state.value.email
        if (email.isBlank()) return
        val subject = URLEncoder.encode(_state.value.emailSubject, StandardCharsets.UTF_8.toString())
        val body = URLEncoder.encode(_state.value.emailBodyTemplate, StandardCharsets.UTF_8.toString())
        val uri = "mailto:$email?subject=$subject&body=$body".toUri()

        emitEffect(SupportEffect.OpenIntent(Intent(Intent.ACTION_SENDTO, uri)))
    }

    private fun SupportCenter.toUiState(): SupportUiState {
        val zoneId = runCatching { ZoneId.of(operationalTimezone) }.getOrDefault(ZoneId.of("Asia/Jakarta"))
        val currentHour = ZonedDateTime.now(zoneId).hour
        val isOnlineNow = currentHour in operationalStartHour until operationalEndHour
        return SupportUiState(
            isLoading = false,
            phone = phone,
            email = email,
            whatsapp = whatsapp,
            whatsappMessageTemplate = whatsappMessageTemplate,
            emailSubject = emailSubject,
            emailBodyTemplate = emailBodyTemplate,
            operationalHoursLabel = operationalHoursLabel,
            statusLabel = if (isOnlineNow) statusOnlineLabel else statusOfflineLabel,
            isOnline = isOnlineNow
        )
    }

    private fun showError(error: UiError) {
        handleSessionError(
            error = error,
            sessionManager = sessionManager,
            beforeLogout = { _state.update { it.copy(isLoading = false) } }
        ) {
            setDialog(
                UiDialog.Center(
                    state = DialogStateV1(
                        type = DialogType.ERROR,
                        title = ErrorMessages.GENERIC_ERROR,
                        message = it.message
                    ),
                    onPrimary = { dismissDialog() }
                )
            )
        }
    }
}
