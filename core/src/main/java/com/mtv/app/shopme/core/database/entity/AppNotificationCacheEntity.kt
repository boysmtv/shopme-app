package com.mtv.app.shopme.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_notification_cache")
data class AppNotificationCacheEntity(
    @PrimaryKey val cacheKey: String,
    val scope: String,
    val notificationId: String,
    val title: String,
    val message: String,
    val createdAt: String,
    val isRead: Boolean,
    val updatedAt: Long
)
