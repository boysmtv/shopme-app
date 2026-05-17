/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodRepository.kt
 *
 * Last modified by Dedy Wijaya on 01/04/26 10.42
 */

package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.model.PagedData
import com.mtv.app.shopme.domain.model.SearchFood
import com.mtv.app.shopme.domain.param.FoodUpsertParam
import com.mtv.app.shopme.domain.param.SearchParam
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    fun getFoods(): Flow<Resource<List<Food>>>

    fun getFoodDetail(id: String): Flow<Resource<Food>>

    fun getFoodsByCafe(id: String): Flow<Resource<List<Food>>>

    fun getFoodsByCafe(
        id: String,
        page: Int,
        size: Int,
        query: String = "",
        category: FoodCategory? = null,
        status: FoodStatus? = null,
        active: Boolean? = null
    ): Flow<Resource<PagedData<Food>>>

    fun getSimilarFoods(cafeId: String): Flow<Resource<List<Food>>>

    fun searchFoods(request: SearchParam): Flow<Resource<PagedData<SearchFood>>>

    fun createFood(param: FoodUpsertParam): Flow<Resource<Unit>>

    fun updateFood(foodId: String, param: FoodUpsertParam): Flow<Resource<Unit>>

    fun deleteFood(foodId: String): Flow<Resource<Unit>>

}
