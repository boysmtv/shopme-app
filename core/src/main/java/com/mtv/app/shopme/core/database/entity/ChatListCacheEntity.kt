package com.mtv.app.shopme.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_list_cache")
data class ChatListCacheEntity(
    @PrimaryKey val cacheKey: String,
    val scope: String,
    val conversationId: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int,
    val avatarBase64: String?,
    val updatedAt: Long
)
