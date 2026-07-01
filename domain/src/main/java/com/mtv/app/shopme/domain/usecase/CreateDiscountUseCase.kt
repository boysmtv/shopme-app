package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.DiscountParam
import com.mtv.app.shopme.domain.repository.SellerRepository
import javax.inject.Inject

class CreateDiscountUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    operator fun invoke(param: DiscountParam) = repository.createDiscount(param)
}
