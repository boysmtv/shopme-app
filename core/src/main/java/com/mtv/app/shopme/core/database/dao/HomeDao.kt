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
import com.mtv.app.shopme.core.database.entity.CustomerEntity
import com.mtv.app.shopme.core.database.entity.FoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {

    @Query("SELECT * FROM customer LIMIT 1")
    fun getCustomer(): Flow<CustomerEntity?>

    @Query("SELECT * FROM customer LIMIT 1")
    suspend fun getCustomerOnce(): CustomerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(data: CustomerEntity)

    @Query("SELECT * FROM food")
    fun getFoods(): Flow<List<FoodEntity>>

    @Query("SELECT * FROM food")
    suspend fun getFoodsOnce(): List<FoodEntity>

    @Query("DELETE FROM food")
    suspend fun clearFoods()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(data: List<FoodEntity>)
}
