package com.mtv.app.shopme.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_mutation")
data class PendingMutationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val actionType: String,
    val payload: String,
    val createdAt: Long,
    val updatedAt: Long,
    val attemptCount: Int,
    val lastError: String?
)
