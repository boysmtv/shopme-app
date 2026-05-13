package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.datasource.OrderRemoteDataSource
import com.mtv.app.shopme.data.remote.response.OrderResponse
import com.mtv.app.shopme.data.remote.response.OrderSummaryResponse
import com.mtv.app.shopme.data.utils.PayloadCacheStore
import com.mtv.app.shopme.domain.repository.OrderRepository
import com.mtv.based.core.network.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.builtins.ListSerializer

class OrderRepositoryImpl @Inject constructor(
    private val remoteDataSource: OrderRemoteDataSource,
    private val resultFlow: ResultFlowFactory,
    private val homeDao: HomeDao,
    private val errorMapper: ErrorMapper
) : OrderRepository {

    override fun getOrders() =
        flow {
            emit(Resource.Loading)
            val cached = PayloadCacheStore.read(
                homeDao = homeDao,
                cacheKey = CUSTOMER_ORDERS_CACHE_KEY,
                serializer = ListSerializer(OrderSummaryResponse.serializer())
            ).orEmpty()
            if (cached.isNotEmpty()) {
                emit(Resource.Success(cached.map { it.toDomain() }))
            }

            try {
                val remoteOrders = remoteDataSource.getOrders()
                PayloadCacheStore.write(
                    homeDao = homeDao,
                    cacheKey = CUSTOMER_ORDERS_CACHE_KEY,
                    serializer = ListSerializer(OrderSummaryResponse.serializer()),
                    value = remoteOrders
                )
                emit(Resource.Success(remoteOrders.map { it.toDomain() }))
            } catch (throwable: Throwable) {
                if (cached.isEmpty()) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun getOrderDetail(orderId: String) =
        flow {
            emit(Resource.Loading)
            val cacheKey = orderDetailCacheKey(orderId)
            val cached = PayloadCacheStore.read(
                homeDao = homeDao,
                cacheKey = cacheKey,
                serializer = OrderResponse.serializer()
            )
            if (cached != null) {
                emit(Resource.Success(cached.toDomain()))
            }

            try {
                val remoteOrder = remoteDataSource.getOrderDetail(orderId)
                PayloadCacheStore.write(
                    homeDao = homeDao,
                    cacheKey = cacheKey,
                    serializer = OrderResponse.serializer(),
                    value = remoteOrder
                )
                emit(Resource.Success(remoteOrder.toDomain()))
            } catch (throwable: Throwable) {
                if (cached == null) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun confirmTransfer(orderId: String) =
        resultFlow.create {
            remoteDataSource.confirmTransfer(orderId)
            PayloadCacheStore.clear(homeDao, CUSTOMER_ORDERS_CACHE_KEY)
            PayloadCacheStore.clear(homeDao, orderDetailCacheKey(orderId))
        }

    private fun orderDetailCacheKey(orderId: String) = "orders:detail:$orderId"

    companion object {
        private const val CUSTOMER_ORDERS_CACHE_KEY = "orders:list:customer"
    }
}
