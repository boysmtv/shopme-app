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
import com.mtv.app.shopme.core.database.entity.CustomerEntity
import com.mtv.app.shopme.core.database.entity.FoodEntity

@Database(
    entities = [CustomerEntity::class, FoodEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun homeDao(): HomeDao
}
