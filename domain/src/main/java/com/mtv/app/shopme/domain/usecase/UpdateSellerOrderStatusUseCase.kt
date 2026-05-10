package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.repository.SellerRepository
import javax.inject.Inject

class UpdateSellerOrderStatusUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    operator fun invoke(orderId: String, status: OrderStatus) = repository.updateOrderStatus(orderId, status)
}
