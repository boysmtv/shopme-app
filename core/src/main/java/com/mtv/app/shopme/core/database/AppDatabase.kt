/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppDatabase.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 19.37
 */

package com.mtv.app.shopme.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.database.entity.AppNotificationCacheEntity
import com.mtv.app.shopme.core.database.entity.ChatListCacheEntity
import com.mtv.app.shopme.core.database.entity.CustomerEntity
import com.mtv.app.shopme.core.database.entity.FoodEntity
import com.mtv.app.shopme.core.database.entity.PayloadCacheEntity

@Database(
    entities = [CustomerEntity::class, FoodEntity::class, ChatListCacheEntity::class, AppNotificationCacheEntity::class, PayloadCacheEntity::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun homeDao(): HomeDao
}
