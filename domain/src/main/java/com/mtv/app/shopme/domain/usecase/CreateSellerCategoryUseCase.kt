package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.SellerCategoryParam
import com.mtv.app.shopme.domain.repository.SellerRepository
import javax.inject.Inject

class CreateSellerCategoryUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    operator fun invoke(param: SellerCategoryParam) = repository.createCategory(param)
}
