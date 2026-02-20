/*
 * Project: App Movie Compose
 * Author: Boys.mtv@gmail.com
 * File: NotificationResponse.kt
 *
 * Last modified by Dedy Wijaya on 10/02/26 15.03
 */

package com.mtv.app.shopme.data.response

import java.util.Date

data class NotificationData(
    val title: String,
    val message: String,
    val date: Date,
    val isRead: Boolean,
    val uid: String
)