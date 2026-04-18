/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ContactSupportViewModel.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 10.38
 */

package com.mtv.app.shopme.feature.customer.presentation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.mtv.app.shopme.core.base.BaseEventViewModel
import com.mtv.app.shopme.feature.customer.contract.SupportEffect
import com.mtv.app.shopme.feature.customer.contract.SupportEvent
import com.mtv.app.shopme.feature.customer.contract.SupportUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.util.Calendar
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
            is SupportEvent.OpenEmail -> {}
            is SupportEvent.OpenDial -> openDial()

            is SupportEvent.ClickBack -> emitEffect(SupportEffect.NavigateBack)
            is SupportEvent.ClickLiveChat -> emitEffect(SupportEffect.NavigateLiveChat)
        }
    }

    private fun emitSafeIntent(
        intent: Intent,
        pm: PackageManager,
        fallbackUrl: String? = null
    ) {
        val canOpen = intent.resolveActivity(pm) != null

        val finalIntent = when {
            canOpen || intent.action == Intent.ACTION_CHOOSER -> intent
            fallbackUrl != null -> Intent(Intent.ACTION_VIEW, fallbackUrl.toUri())
            else -> null
        }

        finalIntent?.let {
            emitEffect(SupportEffect.OpenIntent(it))
        }
    }

    private fun openWhatsapp() {

    }

    private fun openWhatsapp(pm: PackageManager) {
        val number = _state.value.whatsapp
        val uri = "https://wa.me/$number".toUri()

        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.whatsapp")
        }

        emitSafeIntent(intent, pm, uri.toString())
    }

    private fun openDial() {
        val phone = _state.value.phone
        val intent = Intent(Intent.ACTION_DIAL, "tel:$phone".toUri())

        emitEffect(SupportEffect.OpenIntent(intent))
    }

    private fun openEmailWithAttachment(
        context: Context,
        pm: PackageManager
    ) {
        val email = _state.value.email
        val authority = context.packageName + ".provider"

        val screenshotFile = File(context.cacheDir, "support.png")
        val logFile = File(context.cacheDir, "support_log.txt")

        val uris = ArrayList<Uri>()

        if (screenshotFile.exists()) {
            uris.add(FileProvider.getUriForFile(context, authority, screenshotFile))
        }

        if (logFile.exists()) {
            uris.add(FileProvider.getUriForFile(context, authority, logFile))
        }

        val intent = if (uris.isEmpty()) {
            Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:$email".toUri()
                putExtra(Intent.EXTRA_SUBJECT, "Support Request")
                putExtra(Intent.EXTRA_TEXT, "Mohon bantuan terkait aplikasi.")
            }
        } else {
            Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                type = "*/*"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, "Support Request")
                putExtra(Intent.EXTRA_TEXT, "Mohon bantuan terkait aplikasi.")
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }

        val chooser = Intent.createChooser(intent, "Kirim Email")
        emitSafeIntent(chooser, pm)
    }

    private fun isSupportOpen(): Boolean {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return hour in 8..21
    }
}