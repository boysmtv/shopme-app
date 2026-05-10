package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.CafeAddParam
import com.mtv.app.shopme.domain.repository.CafeRepository
import javax.inject.Inject

class CreateCafeUseCase @Inject constructor(
    private val repository: CafeRepository
) {
    operator fun invoke(param: CafeAddParam) = repository.createCafe(param)
}
