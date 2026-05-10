package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.CafeAddParam
import com.mtv.app.shopme.domain.repository.CafeRepository
import javax.inject.Inject

class UpdateCafeUseCase @Inject constructor(
    private val repository: CafeRepository
) {
    operator fun invoke(cafeId: String, param: CafeAddParam) = repository.updateCafe(cafeId, param)
}
