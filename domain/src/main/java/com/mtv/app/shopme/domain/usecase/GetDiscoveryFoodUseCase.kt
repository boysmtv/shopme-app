package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.DiscoveryParam
import com.mtv.app.shopme.domain.repository.FoodRepository
import javax.inject.Inject

class GetDiscoveryFoodUseCase @Inject constructor(
    private val repository: FoodRepository
) {
    operator fun invoke(param: DiscoveryParam) = repository.discoverFoods(param)
}
