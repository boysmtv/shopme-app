package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.datasource.SellerRemoteDataSource
import com.mtv.app.shopme.data.remote.response.OrderResponse
import com.mtv.app.shopme.data.remote.response.SellerOrderSummaryResponse
import com.mtv.app.shopme.data.remote.response.SellerPaymentMethodResponse
import com.mtv.app.shopme.data.remote.response.SellerProfileResponse
import com.mtv.app.shopme.data.utils.PayloadCacheStore
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.param.SellerPaymentMethodParam
import com.mtv.app.shopme.domain.repository.SellerRepository
import com.mtv.based.core.network.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.builtins.ListSerializer

class SellerRepositoryImpl @Inject constructor(
    private val remote: SellerRemoteDataSource,
    private val resultFlow: ResultFlowFactory,
    private val homeDao: HomeDao,
    private val errorMapper: ErrorMapper
) : SellerRepository {

    override fun getProfile() =
        flow {
            emit(Resource.Loading)
            val cached = PayloadCacheStore.read(
                homeDao = homeDao,
                cacheKey = SELLER_PROFILE_CACHE_KEY,
                serializer = SellerProfileResponse.serializer()
            )
            if (cached != null) {
                emit(Resource.Success(cached.toDomain()))
            }

            try {
                val remoteProfile = remote.getProfile()
                PayloadCacheStore.write(
                    homeDao = homeDao,
                    cacheKey = SELLER_PROFILE_CACHE_KEY,
                    serializer = SellerProfileResponse.serializer(),
                    value = remoteProfile
                )
                emit(Resource.Success(remoteProfile.toDomain()))
            } catch (throwable: Throwable) {
                if (cached == null) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun getPaymentMethods() =
        flow {
            emit(Resource.Loading)
            val cached = PayloadCacheStore.read(
                homeDao = homeDao,
                cacheKey = SELLER_PAYMENT_CACHE_KEY,
                serializer = SellerPaymentMethodResponse.serializer()
            )
            if (cached != null) {
                emit(Resource.Success(cached.toDomain()))
            }

            try {
                val remotePayment = remote.getPaymentMethods()
                PayloadCacheStore.write(
                    homeDao = homeDao,
                    cacheKey = SELLER_PAYMENT_CACHE_KEY,
                    serializer = SellerPaymentMethodResponse.serializer(),
                    value = remotePayment
                )
                emit(Resource.Success(remotePayment.toDomain()))
            } catch (throwable: Throwable) {
                if (cached == null) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun getOrders() =
        flow {
            emit(Resource.Loading)
            val cached = PayloadCacheStore.read(
                homeDao = homeDao,
                cacheKey = SELLER_ORDERS_CACHE_KEY,
                serializer = ListSerializer(SellerOrderSummaryResponse.serializer())
            ).orEmpty()
            if (cached.isNotEmpty()) {
                emit(Resource.Success(cached.map { it.toDomain() }))
            }

            try {
                val remoteOrders = remote.getOrders()
                PayloadCacheStore.write(
                    homeDao = homeDao,
                    cacheKey = SELLER_ORDERS_CACHE_KEY,
                    serializer = ListSerializer(SellerOrderSummaryResponse.serializer()),
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
            val cacheKey = sellerOrderDetailCacheKey(orderId)
            val cached = PayloadCacheStore.read(
                homeDao = homeDao,
                cacheKey = cacheKey,
                serializer = OrderResponse.serializer()
            )
            if (cached != null) {
                emit(Resource.Success(cached.toDomain()))
            }

            try {
                val remoteOrder = remote.getOrderDetail(orderId)
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

    override fun updateOrderStatus(orderId: String, status: OrderStatus) =
        resultFlow.create {
            remote.updateOrderStatus(orderId, status)
            PayloadCacheStore.clear(homeDao, SELLER_ORDERS_CACHE_KEY)
            PayloadCacheStore.clear(homeDao, sellerOrderDetailCacheKey(orderId))
        }

    override fun updateAvailability(isOnline: Boolean) =
        resultFlow.create {
            remote.updateAvailability(isOnline).also {
                PayloadCacheStore.write(
                    homeDao = homeDao,
                    cacheKey = SELLER_PROFILE_CACHE_KEY,
                    serializer = SellerProfileResponse.serializer(),
                    value = it
                )
            }.toDomain()
        }

    override fun updatePaymentMethods(param: SellerPaymentMethodParam) =
        resultFlow.create {
            remote.updatePaymentMethods(param).also {
                PayloadCacheStore.write(
                    homeDao = homeDao,
                    cacheKey = SELLER_PAYMENT_CACHE_KEY,
                    serializer = SellerPaymentMethodResponse.serializer(),
                    value = it
                )
            }.toDomain()
        }

    private fun sellerOrderDetailCacheKey(orderId: String) = "seller:order:detail:$orderId"

    companion object {
        private const val SELLER_PROFILE_CACHE_KEY = "seller:profile"
        private const val SELLER_PAYMENT_CACHE_KEY = "seller:payment"
        private const val SELLER_ORDERS_CACHE_KEY = "seller:orders"
    }
}
