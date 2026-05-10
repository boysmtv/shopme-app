package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.FoodRepository
import javax.inject.Inject

class DeleteFoodUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    operator fun invoke(foodId: String) = repository.deleteFood(foodId)
}
