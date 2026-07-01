package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.SellerCategoryParam
import com.mtv.app.shopme.domain.repository.SellerRepository
import javax.inject.Inject

class UpdateSellerCategoryUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    operator fun invoke(categoryId: String, param: SellerCategoryParam) = repository.updateCategory(categoryId, param)
}
