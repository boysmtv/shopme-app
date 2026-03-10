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
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.shopme.data.local.NotificationItem
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

class NotificationServices : FirebaseMessagingService() {

    private val repository by lazy { NotificationRepository(SecurePrefs(applicationContext)) }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        val title = data["title"] ?: EMPTY_STRING
        val message = data["message"] ?: EMPTY_STRING
        val photo = data["photo"] ?: EMPTY_STRING
        val signatureName = data["signature_name"] ?: EMPTY_STRING
        val signatureDate = data["signature_date"] ?: EMPTY_STRING
        val signatureTime = data["signature_time"] ?: EMPTY_STRING
        val isRead = data["isRead"] ?: false

        repository.saveNotification(
            NotificationItem(
                title = title,
                message = message,
                photo = photo,
                signatureName = signatureName,
                signatureDate = signatureDate,
                signatureTime = signatureTime,
                isRead = isRead as Boolean
            )
        )

        applicationContext.showNotification(
            title = title,
            message = message
        )
    }

    override fun onNewToken(token: String) {
        repository.saveStatus(token.isEmpty())
        repository.saveToken(token)
        Log.e("LOG_BOYS_FCM", "Token-onNewToken: $token")
    }
}


