package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.SellerRepository
import javax.inject.Inject

class DeleteSellerCategoryUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    operator fun invoke(categoryId: String) = repository.deleteCategory(categoryId)
}
