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
import androidx.lifecycle.viewModelScope
import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.customer.contract.SupportDataListener
import com.mtv.app.shopme.feature.customer.contract.SupportStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SupportViewModel @Inject constructor() :
    BaseViewModel(),
    UiOwner<SupportStateListener, SupportDataListener> {

    override val uiState = MutableStateFlow(SupportStateListener())

    override val uiData = MutableStateFlow(SupportDataListener())

    private val _intentFlow = MutableSharedFlow<Intent>()
    val intentFlow = _intentFlow.asSharedFlow()

    private fun sendIntent(intent: Intent) {
        viewModelScope.launch { _intentFlow.emit(intent) }
    }

    fun openSafeIntent(
        intent: Intent,
        packageManager: PackageManager,
        fallbackUrl: String? = null
    ) {
        val canOpen = intent.resolveActivity(packageManager) != null

        if (canOpen || intent.action == Intent.ACTION_CHOOSER) {
            sendIntent(intent)
        } else if (fallbackUrl != null) {
            sendIntent(Intent(Intent.ACTION_VIEW, fallbackUrl.toUri()))
        }
    }

    fun openWhatsapp(number: String, pm: PackageManager) {
        val uri = "https://wa.me/$number".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.whatsapp")
        }
        openSafeIntent(intent, pm, fallbackUrl = uri.toString())
    }

    fun openDial(phone: String, pm: PackageManager) {
        val intent = Intent(Intent.ACTION_DIAL, "tel:$phone".toUri())
        openSafeIntent(intent, pm)
    }

    fun openEmailWithAttachment(
        context: Context,
        pm: PackageManager
    ) {
        val email = uiData.value.email
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
        openSafeIntent(chooser, pm)
    }

    fun isSupportOpen(): Boolean {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return hour in 8..21
    }

    fun buildTicketBody(
        userId: String,
        appVersion: String,
        device: String,
        message: String
    ): String {
        return """
            Support Ticket
            
            User ID : $userId
            Device  : $device
            App Ver : $appVersion
            
            Message :
            $message
            
            ---- Auto Generated ----
        """.trimIndent()
    }
}