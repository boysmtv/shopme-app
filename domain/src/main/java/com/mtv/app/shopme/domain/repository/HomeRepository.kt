/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeRepository.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 00.58
 */

package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.Food
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun getCustomer(): Flow<Resource<Customer>>

    fun getFoods(): Flow<Resource<List<Food>>>

}