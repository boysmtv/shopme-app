package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.FoodRepository
import javax.inject.Inject

class GetProductStatsUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    operator fun invoke(cafeId: String) = repository.getProductStats(cafeId)
}
