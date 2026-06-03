package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrderHistoryUseCase @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) {
    operator fun invoke() = getOrdersUseCase()
    operator fun invoke(page: Int, size: Int) = getOrdersUseCase(page, size)
}
