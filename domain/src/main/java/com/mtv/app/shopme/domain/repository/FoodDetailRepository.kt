/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodDetailRepository.kt
 *
 * Last modified by Dedy Wijaya on 27/03/26 22.38
 */

package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.model.param.CartAddParam
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FoodDetailRepository {

    fun getFoodDetail(id: String): Flow<Resource<Food>>

    fun getSimilarFoods(cafeId: String): Flow<Resource<List<Food>>>

    fun addToCart(param: CartAddParam): Flow<Resource<Unit>>

}