package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.Order
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrders(): Flow<Resource<List<Order>>>
    fun getOrderDetail(orderId: String): Flow<Resource<Order>>
    fun confirmTransfer(orderId: String): Flow<Resource<Unit>>
}
