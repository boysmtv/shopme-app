package com.mtv.app.shopme.data.utils

import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.database.entity.PayloadCacheEntity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

object PayloadCacheStore {
    val json: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    suspend fun <T> read(
        homeDao: HomeDao,
        cacheKey: String,
        serializer: KSerializer<T>
    ): T? = homeDao.getPayloadOnce(cacheKey)?.payload?.let {
        json.decodeFromString(serializer, it)
    }

    suspend fun <T> write(
        homeDao: HomeDao,
        cacheKey: String,
        serializer: KSerializer<T>,
        value: T
    ) {
        homeDao.insertPayload(
            PayloadCacheEntity(
                cacheKey = cacheKey,
                payload = json.encodeToString(serializer, value),
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun clear(homeDao: HomeDao, cacheKey: String) {
        homeDao.clearPayload(cacheKey)
    }

    fun <T> listSerializer(serializer: KSerializer<T>): KSerializer<List<T>> = ListSerializer(serializer)
}
