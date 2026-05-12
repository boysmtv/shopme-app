package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.ProfileRepository
import javax.inject.Inject

class GetFavoriteFoodIdsUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke() = repository.getFavoriteFoodIds()
}
