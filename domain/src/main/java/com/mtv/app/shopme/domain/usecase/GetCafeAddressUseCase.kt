package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.CafeRepository
import javax.inject.Inject

class GetCafeAddressUseCase @Inject constructor(
    private val repository: CafeRepository
) {
    operator fun invoke(cafeId: String) = repository.getCafeAddress(cafeId)
}
