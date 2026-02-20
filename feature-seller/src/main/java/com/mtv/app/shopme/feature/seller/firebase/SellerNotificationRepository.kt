/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SellerNotificationRepository.kt
 *
 * Last modified by Dedy Wijaya on 20/02/26 21.28
 */

package com.mtv.app.shopme.feature.seller.firebase

import com.mtv.app.core.provider.utils.SecurePrefs
import com.mtv.app.shopme.common.ConstantPreferences.FCM_SELLER_NOTIFICATION
import com.mtv.app.shopme.feature.seller.model.SellerNotifItem

class SellerNotificationRepository(
    private val securePrefs: SecurePrefs
) {

    fun saveNotification(item: SellerNotifItem) {
        val current = getAllNotifications().toMutableList()
        current.add(0, item)
        securePrefs.putObject(
            FCM_SELLER_NOTIFICATION,
            current.toTypedArray()
        )
    }

    fun getAllNotifications(): List<SellerNotifItem> {
        return securePrefs.getObject(
            FCM_SELLER_NOTIFICATION,
            Array<SellerNotifItem>::class.java
        )?.toList() ?: emptyList()
    }
}