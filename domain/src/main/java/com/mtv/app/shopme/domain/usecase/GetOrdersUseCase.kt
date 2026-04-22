package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke() = repository.getOrders()
}
