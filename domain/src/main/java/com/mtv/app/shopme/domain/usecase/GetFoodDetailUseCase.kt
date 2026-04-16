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

class GetFoodDetailUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    operator fun invoke(id: String) = repository.getFoodDetail(id)
}
