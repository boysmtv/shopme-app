/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeLocalDataSource.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 19.43
 */

package com.mtv.app.shopme.data.local.home

import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeLocalDataSource @Inject constructor(
    private val dao: HomeDao
) {

    fun getCustomer(): Flow<Customer?> =
        dao.getCustomer().map { it?.toDomain() }

    suspend fun saveCustomer(customer: Customer) {
        dao.insertCustomer(customer.toEntity())
    }

    fun getFoods(): Flow<List<Food>> =
        dao.getFoods().map { list ->
            list.map { it.toDomain() }
        }

    suspend fun saveFoods(foods: List<Food>) {
        dao.insertFoods(foods.map { it.toEntity() })
    }
}