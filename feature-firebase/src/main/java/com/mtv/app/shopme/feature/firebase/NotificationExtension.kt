/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotificationExtension.kt
 *
 * Last modified by Dedy Wijaya on 05/03/26 10.54
 */

package com.mtv.app.shopme.feature.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mtv.app.shopme.common.R

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