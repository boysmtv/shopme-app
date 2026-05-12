package com.mtv.app.shopme.data.sync

import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.database.entity.PendingMutationEntity
import kotlinx.serialization.json.Json

class PendingMutationStore(
    private val homeDao: HomeDao
) {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        classDiscriminator = "type"
    }

    suspend fun enqueue(action: PendingMutationAction) {
        val now = System.currentTimeMillis()
        homeDao.insertPendingMutation(
            PendingMutationEntity(
                actionType = action::class.simpleName.orEmpty(),
                payload = json.encodeToString(PendingMutationAction.serializer(), action),
                createdAt = now,
                updatedAt = now,
                attemptCount = 0,
                lastError = null
            )
        )
    }

    suspend fun list(): List<PendingMutationRecord> = homeDao.getPendingMutations().map { entity ->
        PendingMutationRecord(
            entity = entity,
            action = json.decodeFromString(PendingMutationAction.serializer(), entity.payload)
        )
    }

    suspend fun delete(id: Long) {
        homeDao.deletePendingMutation(id)
    }

    suspend fun markFailed(id: Long, attemptCount: Int, lastError: String?) {
        homeDao.updatePendingMutationState(
            id = id,
            updatedAt = System.currentTimeMillis(),
            attemptCount = attemptCount,
            lastError = lastError
        )
    }
}

data class PendingMutationRecord(
    val entity: PendingMutationEntity,
    val action: PendingMutationAction
)
