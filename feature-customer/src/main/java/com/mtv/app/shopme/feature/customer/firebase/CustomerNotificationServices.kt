/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CustomerNotificationServices.kt
 *
 * Last modified by Dedy Wijaya on 10/02/26 15.16
 */

package com.mtv.app.shopme.feature.customer.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.shopme.common.ConstantPreferences.FCM_CUSTOMER_NOTIFICATION
import com.mtv.app.shopme.common.ConstantPreferences.FCM_STATUS
import com.mtv.app.shopme.common.ConstantPreferences.FCM_TOKEN
import com.mtv.app.shopme.common.R
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

data class NotifItem(
    val title: String,
    val message: String,
    val photo: String,
    val signatureName: String,
    val signatureDate: String,
    val signatureTime: String,
    val isRead: Boolean,
)

class CustomerNotificationServices : FirebaseMessagingService() {

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
            NotifItem(
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

class NotificationRepository(private val securePrefs: SecurePrefs) {

    fun saveStatus(status: Boolean) {
        securePrefs.putBoolean(FCM_STATUS, status)
    }

    fun saveToken(token: String) {
        securePrefs.putString(FCM_TOKEN, token)
    }

    fun saveNotification(item: NotifItem) {
        val current = getAllNotifications().toMutableList()
        current.add(0, item)
        securePrefs.putObject(FCM_CUSTOMER_NOTIFICATION, current.toTypedArray())
    }

    fun getAllNotifications(): List<NotifItem> {
        return securePrefs.getObject(FCM_CUSTOMER_NOTIFICATION, Array<NotifItem>::class.java)?.toList()
            ?: emptyList()
    }
}

fun Context.showNotification(title: String, message: String, channelId: String = "default_channel") {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Default Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(this, channelId)
        .setContentTitle(title)
        .setContentText(message)
        .setSmallIcon(R.drawable.image_cheese_burger)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(System.currentTimeMillis().toInt(), notification)
}