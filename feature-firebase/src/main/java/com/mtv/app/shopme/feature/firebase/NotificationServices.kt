/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotificationServices.kt
 *
 * Last modified by Dedy Wijaya on 10/02/26 15.16
 */

package com.mtv.app.shopme.feature.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.app.shopme.common.notification.NotificationDeepLink
import com.mtv.app.shopme.domain.model.NotificationItem
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

class NotificationServices : FirebaseMessagingService() {

    private val repository by lazy { NotificationRepository(SecurePrefs(applicationContext)) }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        val title = data["title"]
            ?: remoteMessage.notification?.title
            ?: data["type"]?.toPushTitle()
            ?: EMPTY_STRING
        val message = data["message"]
            ?: remoteMessage.notification?.body
            ?: data.toPushMessage()
        val photo = data["photo"] ?: EMPTY_STRING
        val signatureName = data["signature_name"]
            ?: data["actorName"]
            ?: data["customerName"]
            ?: data["cafeName"]
            ?: EMPTY_STRING
        val signatureDate = data["signature_date"] ?: EMPTY_STRING
        val signatureTime = data["signature_time"] ?: EMPTY_STRING
        val isRead = data["isRead"]?.toBooleanStrictOrNull() ?: false
        val deepLink = NotificationDeepLink.from(data)

        repository.saveNotification(
            NotificationItem(
                title = title,
                message = message,
                photo = photo,
                signatureName = signatureName,
                signatureDate = signatureDate,
                signatureTime = signatureTime,
                isRead = isRead
            )
        )

        applicationContext.showNotification(
            title = title,
            message = message,
            deepLink = deepLink
        )
    }

    override fun onNewToken(token: String) {
        repository.saveStatus(token.isEmpty())
        repository.saveToken(token)
        Log.e("LOG_BOYS_FCM", "Token-onNewToken: $token")
    }
}

private fun String.toPushTitle(): String = when (this) {
    "order_created" -> "Pesanan baru"
    "payment_confirmation" -> "Konfirmasi pembayaran"
    "order_status_updated" -> "Status pesanan"
    "chat_message" -> "Chat baru"
    else -> "Shopme"
}

private fun Map<String, String>.toPushMessage(): String {
    val actor = this["actorName"]
        ?: this["customerName"]
        ?: this["cafeName"]
        ?: this["sellerName"]
    val items = this["orderItemsSummary"]
    val address = this["deliveryAddress"]
    val status = listOf(this["orderStatus"], this["paymentStatus"])
        .filter { !it.isNullOrBlank() }
        .joinToString(" / ")

    return listOfNotNull(actor, items, address, status.takeIf { it.isNotBlank() })
        .joinToString(" - ")
        .ifBlank { "Ada update baru di Shopme" }
}


