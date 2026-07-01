package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.DiscountParam
import com.mtv.app.shopme.domain.repository.SellerRepository
import javax.inject.Inject

class UpdateDiscountUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    operator fun invoke(discountId: String, param: DiscountParam) = repository.updateDiscount(discountId, param)
}
