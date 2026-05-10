package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.FoodUpsertParam
import com.mtv.app.shopme.domain.repository.FoodRepository
import javax.inject.Inject

class UpdateFoodUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    operator fun invoke(foodId: String, param: FoodUpsertParam) = repository.updateFood(foodId, param)
}
