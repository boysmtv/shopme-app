package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.SellerRepository
import javax.inject.Inject

class CancelSellerOrderUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    operator fun invoke(orderId: String, reason: String?) = repository.cancelOrder(orderId, reason)
}
