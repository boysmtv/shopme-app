package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.CafeAddressUpsertParam
import com.mtv.app.shopme.domain.repository.CafeRepository
import javax.inject.Inject

class UpsertCafeAddressUseCase @Inject constructor(
    private val repository: CafeRepository
) {
    operator fun invoke(param: CafeAddressUpsertParam) = repository.upsertCafeAddress(param)
}
