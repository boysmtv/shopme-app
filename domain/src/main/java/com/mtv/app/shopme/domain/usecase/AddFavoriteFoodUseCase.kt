package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.ProfileRepository
import javax.inject.Inject

class AddFavoriteFoodUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(foodId: String) = repository.addFavoriteFood(foodId)
}
