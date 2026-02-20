/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerNotificationService.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.27
 */

package com.mtv.app.shopme.feature.seller.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.feature.seller.model.SellerNotifItem
import com.mtv.based.uicomponent.core.ui.util.Constants.Companion.EMPTY_STRING

class SellerNotificationService : FirebaseMessagingService() {

    private val repository by lazy { SellerNotificationRepository(SecurePrefs(applicationContext)) }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data

        val item = SellerNotifItem(
            title = data["title"] ?: EMPTY_STRING,
            message = data["message"] ?: EMPTY_STRING,
            orderId = data["order_id"] ?: EMPTY_STRING,
            buyerName = data["buyer_name"] ?: EMPTY_STRING,
            date = data["date"] ?: EMPTY_STRING,
            time = data["time"] ?: EMPTY_STRING,
            isRead = false
        )

        repository.saveNotification(item)

        applicationContext.showNotification(
            title = item.title,
            message = item.message
        )
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