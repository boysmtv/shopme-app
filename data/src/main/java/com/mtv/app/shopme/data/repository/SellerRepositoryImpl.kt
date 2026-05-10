package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.datasource.SellerRemoteDataSource
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.param.SellerPaymentMethodParam
import com.mtv.app.shopme.domain.repository.SellerRepository
import javax.inject.Inject

class SellerRepositoryImpl @Inject constructor(
    private val remote: SellerRemoteDataSource,
    private val resultFlow: ResultFlowFactory
) : SellerRepository {

    override fun getProfile() =
        resultFlow.create {
            remote.getProfile().toDomain()
        }

    override fun getPaymentMethods() =
        resultFlow.create {
            remote.getPaymentMethods().toDomain()
        }

    override fun getOrders() =
        resultFlow.create {
            remote.getOrders().map { it.toDomain() }
        }

    override fun getOrderDetail(orderId: String) =
        resultFlow.create {
            remote.getOrderDetail(orderId).toDomain()
        }

    override fun updateOrderStatus(orderId: String, status: OrderStatus) =
        resultFlow.create {
            remote.updateOrderStatus(orderId, status)
        }

    override fun updateAvailability(isOnline: Boolean) =
        resultFlow.create {
            remote.updateAvailability(isOnline).toDomain()
        }

    override fun updatePaymentMethods(param: SellerPaymentMethodParam) =
        resultFlow.create {
            remote.updatePaymentMethods(param).toDomain()
        }
}
