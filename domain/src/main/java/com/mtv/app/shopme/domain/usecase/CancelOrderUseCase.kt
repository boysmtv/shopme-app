package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.OrderRepository
import javax.inject.Inject

class CancelOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke(orderId: String, reason: String?) = repository.cancelOrder(orderId, reason)
}
