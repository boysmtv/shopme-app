package com.mtv.app.shopme.domain.model

data class NotificationItem(
    val title: String,
    val message: String,
    val photo: String,
    val signatureName: String,
    val signatureDate: String,
    val signatureTime: String,
    val isRead: Boolean,
)