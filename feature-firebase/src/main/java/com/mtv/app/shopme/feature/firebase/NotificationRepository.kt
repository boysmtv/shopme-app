/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: NotificationRepository.kt
 *
 * Last modified by Dedy Wijaya on 05/03/26 10.49
 */

package com.mtv.app.shopme.feature.firebase

import com.mtv.based.core.provider.utils.SecurePrefs
import com.mtv.app.shopme.common.ConstantPreferences.FCM_CUSTOMER_NOTIFICATION
import com.mtv.app.shopme.common.ConstantPreferences.FCM_STATUS
import com.mtv.app.shopme.common.ConstantPreferences.FCM_TOKEN
import com.mtv.app.shopme.data.local.NotificationItem

class NotificationRepository(private val securePrefs: SecurePrefs) {

    fun saveStatus(status: Boolean) {
        securePrefs.putBoolean(FCM_STATUS, status)
    }

    fun saveToken(token: String) {
        securePrefs.putString(FCM_TOKEN, token)
    }

    fun saveNotification(item: NotificationItem) {
        val current = getAllNotifications().toMutableList()
        current.add(0, item)
        securePrefs.putObject(FCM_CUSTOMER_NOTIFICATION, current.toTypedArray())
    }

    fun getAllNotifications(): List<NotificationItem> {
        return securePrefs.getObject(FCM_CUSTOMER_NOTIFICATION, Array<NotificationItem>::class.java)?.toList()
            ?: emptyList()
    }
}

