/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodDetailUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 11.39
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.FoodDetailRepository
import javax.inject.Inject

class FoodSimilarUseCase @Inject constructor(
    private val repository: FoodDetailRepository
) {
    operator fun invoke(cafeId: String) = repository.getSimilarFoods(cafeId)
}