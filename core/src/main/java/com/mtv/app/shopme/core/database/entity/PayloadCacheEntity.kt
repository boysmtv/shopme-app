package com.mtv.app.shopme.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payload_cache")
data class PayloadCacheEntity(
    @PrimaryKey val cacheKey: String,
    val payload: String,
    val updatedAt: Long
)
