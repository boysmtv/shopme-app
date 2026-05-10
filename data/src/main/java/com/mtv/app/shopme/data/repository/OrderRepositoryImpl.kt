package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.datasource.OrderRemoteDataSource
import com.mtv.app.shopme.domain.repository.OrderRepository
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val remoteDataSource: OrderRemoteDataSource,
    private val resultFlow: ResultFlowFactory
) : OrderRepository {

    override fun getOrders() =
        resultFlow.create {
            remoteDataSource.getOrders().map { it.toDomain() }
        }

    override fun getOrderDetail(orderId: String) =
        resultFlow.create {
            remoteDataSource.getOrderDetail(orderId).toDomain()
        }

    override fun confirmTransfer(orderId: String) =
        resultFlow.create {
            remoteDataSource.confirmTransfer(orderId)
        }
}
