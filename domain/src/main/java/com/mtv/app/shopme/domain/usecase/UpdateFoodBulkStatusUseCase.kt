package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.FoodBulkStatusParam
import com.mtv.app.shopme.domain.repository.FoodRepository
import javax.inject.Inject

class UpdateFoodBulkStatusUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    operator fun invoke(param: FoodBulkStatusParam) = repository.bulkUpdateActive(param)
}
