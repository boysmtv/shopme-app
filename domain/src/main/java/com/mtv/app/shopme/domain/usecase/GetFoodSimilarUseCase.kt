/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodDetailUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 11.39
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.FoodRepository
import javax.inject.Inject

class GetFoodSimilarUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    operator fun invoke(cafeId: String) = repository.getSimilarFoods(cafeId)
    operator fun invoke(cafeId: String, page: Int, size: Int) = repository.getFoodsByCafe(cafeId, page, size)
}
