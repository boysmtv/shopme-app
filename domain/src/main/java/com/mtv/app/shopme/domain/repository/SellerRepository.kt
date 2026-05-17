package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.Order
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.model.PagedData
import com.mtv.app.shopme.domain.model.SellerPaymentMethod
import com.mtv.app.shopme.domain.model.SellerOrderItem
import com.mtv.app.shopme.domain.model.SellerProfile
import com.mtv.app.shopme.domain.param.SellerPaymentMethodParam
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SellerRepository {
    fun getProfile(): Flow<Resource<SellerProfile>>
    fun getPaymentMethods(): Flow<Resource<SellerPaymentMethod>>
    fun getOrders(): Flow<Resource<List<SellerOrderItem>>>
    fun getOrders(page: Int, size: Int): Flow<Resource<PagedData<SellerOrderItem>>>
    fun getOrderDetail(orderId: String): Flow<Resource<Order>>
    fun updateOrderStatus(orderId: String, status: OrderStatus): Flow<Resource<Unit>>
    fun cancelOrder(orderId: String, reason: String?): Flow<Resource<Unit>>
    fun updateAvailability(isOnline: Boolean): Flow<Resource<SellerProfile>>
    fun updatePaymentMethods(param: SellerPaymentMethodParam): Flow<Resource<SellerPaymentMethod>>
}
