package com.mtv.app.shopme.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_message_cache")
data class ChatMessageCacheEntity(
    @PrimaryKey val cacheKey: String,
    val scope: String,
    val conversationId: String,
    val messageId: String,
    val message: String,
    val time: String,
    val isFromUser: Boolean,
    val isRead: Boolean,
    val position: Int,
    val updatedAt: Long
)
