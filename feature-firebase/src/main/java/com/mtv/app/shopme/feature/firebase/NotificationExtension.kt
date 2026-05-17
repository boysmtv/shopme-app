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
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mtv.app.shopme.common.R
import com.mtv.app.shopme.common.notification.NotificationDeepLink
import com.mtv.app.shopme.common.notification.NotificationDeepLinkExtras

fun Context.showNotification(
    title: String,
    message: String,
    channelId: String = "default_channel",
    deepLink: NotificationDeepLink? = null
) {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Default Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    val pendingIntent = deepLink?.toPendingIntent(this)
    val notification = NotificationCompat.Builder(this, channelId)
        .setContentTitle(title)
        .setContentText(message)
        .setSmallIcon(R.drawable.image_cheese_burger)
        .setAutoCancel(true)
        .apply {
            if (pendingIntent != null) {
                setContentIntent(pendingIntent)
            }
        }
        .build()

    notificationManager.notify(System.currentTimeMillis().toInt(), notification)
}

private fun NotificationDeepLink.toPendingIntent(context: Context): PendingIntent? {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName) ?: return null
    intent
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        .putExtra(NotificationDeepLinkExtras.TYPE, type)
        .putExtra(NotificationDeepLinkExtras.ORDER_ID, orderId)
        .putExtra(NotificationDeepLinkExtras.CONVERSATION_ID, conversationId)
        .putExtra(NotificationDeepLinkExtras.ROLE, role)

    return PendingIntent.getActivity(
        context,
        (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}
