/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: GetFoodsByCafeUseCase.kt
 *
 * Last modified by Dedy Wijaya on 18/03/26 01.29
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.repository.FoodRepository
import javax.inject.Inject

class GetFoodsByCafeUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    operator fun invoke(id: String) = repository.getFoodsByCafe(id)
    operator fun invoke(id: String, page: Int, size: Int) = repository.getFoodsByCafe(id, page, size)
    operator fun invoke(
        id: String,
        page: Int,
        size: Int,
        query: String,
        category: FoodCategory?,
        status: FoodStatus?,
        active: Boolean?
    ) = repository.getFoodsByCafe(id, page, size, query, category, status, active)
}
