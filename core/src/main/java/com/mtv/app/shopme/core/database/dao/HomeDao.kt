/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeDao.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 19.37
 */

package com.mtv.app.shopme.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mtv.app.shopme.core.database.entity.AppNotificationCacheEntity
import com.mtv.app.shopme.core.database.entity.ChatListCacheEntity
import com.mtv.app.shopme.core.database.entity.CustomerEntity
import com.mtv.app.shopme.core.database.entity.FoodEntity
import com.mtv.app.shopme.core.database.entity.PayloadCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {

    @Query("SELECT * FROM customer LIMIT 1")
    fun getCustomer(): Flow<CustomerEntity?>

    @Query("SELECT * FROM customer LIMIT 1")
    suspend fun getCustomerOnce(): CustomerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(data: CustomerEntity)

    @Query("SELECT * FROM chat_list_cache WHERE scope = :scope ORDER BY updatedAt DESC")
    suspend fun getChatListOnce(scope: String): List<ChatListCacheEntity>

    @Query("DELETE FROM chat_list_cache WHERE scope = :scope")
    suspend fun clearChatList(scope: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatList(data: List<ChatListCacheEntity>)

    @Query("SELECT * FROM app_notification_cache WHERE scope = :scope ORDER BY createdAt DESC")
    suspend fun getNotificationsOnce(scope: String): List<AppNotificationCacheEntity>

    @Query("DELETE FROM app_notification_cache WHERE scope = :scope")
    suspend fun clearNotifications(scope: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(data: List<AppNotificationCacheEntity>)

    @Query("SELECT * FROM food")
    fun getFoods(): Flow<List<FoodEntity>>

    @Query("SELECT * FROM food")
    suspend fun getFoodsOnce(): List<FoodEntity>

    @Query("DELETE FROM food")
    suspend fun clearFoods()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(data: List<FoodEntity>)

    @Query("SELECT * FROM payload_cache WHERE cacheKey = :cacheKey LIMIT 1")
    suspend fun getPayloadOnce(cacheKey: String): PayloadCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayload(data: PayloadCacheEntity)

    @Query("DELETE FROM payload_cache WHERE cacheKey = :cacheKey")
    suspend fun clearPayload(cacheKey: String)
}
