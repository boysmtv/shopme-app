/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartDao.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 12.34
 */

package com.mtv.app.shopme.core.database.dao

import androidx.room.*
import com.mtv.app.shopme.core.database.entity.CartEntity
import com.mtv.app.shopme.core.database.entity.CartVariantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart")
    fun getCart(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(data: List<CartEntity>)

    @Query("DELETE FROM cart")
    suspend fun clearCart()

    // 🔥 variant
    @Query("SELECT * FROM cart_variant WHERE cartId = :cartId")
    suspend fun getVariants(cartId: String): List<CartVariantEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariants(data: List<CartVariantEntity>)

    @Query("DELETE FROM cart_variant")
    suspend fun clearVariants()
}