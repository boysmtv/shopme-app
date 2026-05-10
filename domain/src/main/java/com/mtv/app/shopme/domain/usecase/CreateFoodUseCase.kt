package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.FoodUpsertParam
import com.mtv.app.shopme.domain.repository.FoodRepository
import javax.inject.Inject

class CreateFoodUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    operator fun invoke(param: FoodUpsertParam) = repository.createFood(param)
}
