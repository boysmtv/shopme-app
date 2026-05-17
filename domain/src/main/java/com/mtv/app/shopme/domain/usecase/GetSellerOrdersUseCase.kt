package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.SellerRepository
import javax.inject.Inject

class GetSellerOrdersUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    operator fun invoke() = repository.getOrders()
    operator fun invoke(page: Int, size: Int) = repository.getOrders(page, size)
}
