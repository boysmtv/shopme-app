package com.mtv.app.shopme.domain.model

import java.util.Date

data class NotificationData(
    val title: String,
    val message: String,
    val date: Date,
    val isRead: Boolean,
    val uid: String
)