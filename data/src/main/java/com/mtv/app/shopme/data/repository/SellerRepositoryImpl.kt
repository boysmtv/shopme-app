package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.mapper.toRequest
import com.mtv.app.shopme.data.remote.datasource.SellerRemoteDataSource
import com.mtv.app.shopme.data.remote.response.DiscountResponse
import com.mtv.app.shopme.data.remote.response.OrderResponse
import com.mtv.app.shopme.data.remote.response.ReviewResponse
import com.mtv.app.shopme.data.remote.response.SellerCategoryResponse
import com.mtv.app.shopme.data.remote.response.SellerDashboardResponse
import com.mtv.app.shopme.data.remote.response.SellerOrderSummaryResponse
import com.mtv.app.shopme.data.remote.response.SellerPaymentMethodResponse
import com.mtv.app.shopme.data.remote.response.SellerProfileResponse
import com.mtv.app.shopme.data.sync.OfflineMutationSyncManager
import com.mtv.app.shopme.data.sync.PendingMutationAction
import com.mtv.app.shopme.data.utils.PayloadCacheStore
import com.mtv.app.shopme.domain.model.OrderStatus
import com.mtv.app.shopme.domain.model.PagedData
import com.mtv.app.shopme.domain.param.DiscountParam
import com.mtv.app.shopme.domain.param.ReviewReplyParam
import com.mtv.app.shopme.domain.param.SellerCategoryParam
import com.mtv.app.shopme.domain.param.SellerPaymentMethodParam
import com.mtv.app.shopme.domain.repository.SellerRepository
import com.mtv.app.shopme.domain.model.Resource
import com.mtv.app.shopme.data.utils.isRetryableOfflineWrite
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.builtins.ListSerializer

class SellerRepositoryImpl @Inject constructor(
    private val remote: SellerRemoteDataSource,
    private val resultFlow: ResultFlowFactory,
    private val homeDao: HomeDao,
    private val syncManager: OfflineMutationSyncManager
) : SellerRepository {

    override fun getDashboard() =
        flow {
            emit(Resource.Loading)
            try {
                val dashboard = remote.getDashboard()
                emit(Resource.Success(dashboard.toDomain()))
            } catch (throwable: Throwable) {
                emit(Resource.Error(throwable))
            }
        }.flowOn(Dispatchers.IO)

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
                    emit(Resource.Error(throwable))
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
                    emit(Resource.Error(throwable))
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
                    emit(Resource.Error(throwable))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun getOrders(page: Int, size: Int, status: OrderStatus?) =
        flow {
            emit(Resource.Loading)
            try {
                val remoteOrders = remote.getOrders(page, size, status)
                emit(
                    Resource.Success(
                        PagedData(
                            content = remoteOrders.content.map { it.toDomain() },
                            page = remoteOrders.page,
                            last = remoteOrders.last
                        )
                    )
                )
            } catch (throwable: Throwable) {
                emit(Resource.Error(throwable))
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
                    emit(Resource.Error(throwable))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun updateOrderStatus(orderId: String, status: OrderStatus) =
        resultFlow.create {
            remote.updateOrderStatus(orderId, status)
            PayloadCacheStore.clear(homeDao, SELLER_ORDERS_CACHE_KEY)
            PayloadCacheStore.clear(homeDao, sellerOrderDetailCacheKey(orderId))
        }

    override fun cancelOrder(orderId: String, reason: String?) =
        resultFlow.create {
            remote.cancelOrder(orderId, reason)
            PayloadCacheStore.clear(homeDao, SELLER_ORDERS_CACHE_KEY)
            PayloadCacheStore.clear(homeDao, sellerOrderDetailCacheKey(orderId))
        }

    override fun updateAvailability(isOnline: Boolean) =
        flow {
            emit(Resource.Loading)
            try {
                val updated = remote.updateAvailability(isOnline)
                cacheSellerProfile(updated)
                emit(Resource.Success(updated.toDomain()))
            } catch (throwable: Throwable) {
                if (throwable.isRetryableOfflineWrite()) {
                    syncManager.enqueue(
                        PendingMutationAction.SellerAvailability(
                            body = com.mtv.app.shopme.data.remote.request.SellerAvailabilityRequest(isOnline)
                        )
                    )
                    patchSellerAvailability(isOnline)
                    emit(Resource.Success(requireSellerProfileFromCache()))
                } else {
                    emit(Resource.Error(throwable))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun updatePaymentMethods(param: SellerPaymentMethodParam) =
        flow {
            emit(Resource.Loading)
            try {
                val updated = remote.updatePaymentMethods(param)
                cacheSellerPaymentMethods(updated)
                emit(Resource.Success(updated.toDomain()))
            } catch (throwable: Throwable) {
                if (throwable.isRetryableOfflineWrite()) {
                    syncManager.enqueue(
                        PendingMutationAction.SellerPaymentMethods(param.toRequest())
                    )
                    patchSellerPaymentMethods(param)
                    emit(Resource.Success(requireSellerPaymentMethodsFromCache()))
                } else {
                    emit(Resource.Error(throwable))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun getDiscounts() =
        flow {
            emit(Resource.Loading)
            try {
                val discounts = remote.getDiscounts()
                emit(Resource.Success(discounts.map { it.toDomain() }))
            } catch (throwable: Throwable) {
                emit(Resource.Error(throwable))
            }
        }.flowOn(Dispatchers.IO)

    override fun createDiscount(param: DiscountParam) =
        flow {
            emit(Resource.Loading)
            try {
                val discount = remote.createDiscount(param.toRequest())
                emit(Resource.Success(discount.toDomain()))
            } catch (throwable: Throwable) {
                emit(Resource.Error(throwable))
            }
        }.flowOn(Dispatchers.IO)

    override fun updateDiscount(discountId: String, param: DiscountParam) =
        flow {
            emit(Resource.Loading)
            try {
                val discount = remote.updateDiscount(discountId, param.toRequest())
                emit(Resource.Success(discount.toDomain()))
            } catch (throwable: Throwable) {
                emit(Resource.Error(throwable))
            }
        }.flowOn(Dispatchers.IO)

    override fun deleteDiscount(discountId: String) =
        resultFlow.create {
            remote.deleteDiscount(discountId)
        }

    override fun getReviews() =
        flow {
            emit(Resource.Loading)
            try {
                val reviews = remote.getReviews()
                emit(Resource.Success(reviews.map { it.toDomain() }))
            } catch (throwable: Throwable) {
                emit(Resource.Error(throwable))
            }
        }.flowOn(Dispatchers.IO)

    override fun replyToReview(reviewId: String, param: ReviewReplyParam) =
        flow {
            emit(Resource.Loading)
            try {
                val review = remote.replyToReview(reviewId, param.toRequest())
                emit(Resource.Success(review.toDomain()))
            } catch (throwable: Throwable) {
                emit(Resource.Error(throwable))
            }
        }.flowOn(Dispatchers.IO)

    override fun getCategories() =
        flow {
            emit(Resource.Loading)
            try {
                val categories = remote.getCategories()
                emit(Resource.Success(categories.map { it.toDomain() }))
            } catch (throwable: Throwable) {
                emit(Resource.Error(throwable))
            }
        }.flowOn(Dispatchers.IO)

    override fun createCategory(param: SellerCategoryParam) =
        flow {
            emit(Resource.Loading)
            try {
                val category = remote.createCategory(param.toRequest())
                emit(Resource.Success(category.toDomain()))
            } catch (throwable: Throwable) {
                emit(Resource.Error(throwable))
            }
        }.flowOn(Dispatchers.IO)

    override fun updateCategory(categoryId: String, param: SellerCategoryParam) =
        flow {
            emit(Resource.Loading)
            try {
                val category = remote.updateCategory(categoryId, param.toRequest())
                emit(Resource.Success(category.toDomain()))
            } catch (throwable: Throwable) {
                emit(Resource.Error(throwable))
            }
        }.flowOn(Dispatchers.IO)

    override fun deleteCategory(categoryId: String) =
        resultFlow.create {
            remote.deleteCategory(categoryId)
        }

    private fun sellerOrderDetailCacheKey(orderId: String) = "seller:order:detail:$orderId"

    companion object {
        private const val SELLER_PROFILE_CACHE_KEY = "seller:profile"
        private const val SELLER_PAYMENT_CACHE_KEY = "seller:payment"
        private const val SELLER_ORDERS_CACHE_KEY = "seller:orders"
    }

    private suspend fun cacheSellerProfile(value: SellerProfileResponse) {
        PayloadCacheStore.write(
            homeDao = homeDao,
            cacheKey = SELLER_PROFILE_CACHE_KEY,
            serializer = SellerProfileResponse.serializer(),
            value = value
        )
    }

    private suspend fun cacheSellerPaymentMethods(value: SellerPaymentMethodResponse) {
        PayloadCacheStore.write(
            homeDao = homeDao,
            cacheKey = SELLER_PAYMENT_CACHE_KEY,
            serializer = SellerPaymentMethodResponse.serializer(),
            value = value
        )
    }

    private suspend fun patchSellerAvailability(isOnline: Boolean) {
        PayloadCacheStore.read(
            homeDao = homeDao,
            cacheKey = SELLER_PROFILE_CACHE_KEY,
            serializer = SellerProfileResponse.serializer()
        )?.let { cached ->
            cacheSellerProfile(cached.copy(isOnline = isOnline))
        }
    }

    private suspend fun patchSellerPaymentMethods(param: SellerPaymentMethodParam) {
        cacheSellerPaymentMethods(
            SellerPaymentMethodResponse(
                cashEnabled = param.cashEnabled,
                bankEnabled = param.bankEnabled,
                bankNumber = param.bankNumber,
                bankType = param.bankType,
                ovoEnabled = param.ovoEnabled,
                ovoNumber = param.ovoNumber,
                danaEnabled = param.danaEnabled,
                danaNumber = param.danaNumber,
                gopayEnabled = param.gopayEnabled,
                gopayNumber = param.gopayNumber
            )
        )
    }

    private suspend fun requireSellerProfileFromCache() =
        PayloadCacheStore.read(
            homeDao = homeDao,
            cacheKey = SELLER_PROFILE_CACHE_KEY,
            serializer = SellerProfileResponse.serializer()
        )?.toDomain() ?: SellerProfileResponse(
            sellerName = "",
            email = "",
            phone = "",
            storeName = "",
            storeAddress = "",
            isOnline = false,
            hasCafe = false
        ).toDomain()

    private suspend fun requireSellerPaymentMethodsFromCache() =
        PayloadCacheStore.read(
            homeDao = homeDao,
            cacheKey = SELLER_PAYMENT_CACHE_KEY,
            serializer = SellerPaymentMethodResponse.serializer()
        )?.toDomain() ?: SellerPaymentMethodResponse(
            cashEnabled = false,
            bankEnabled = false,
            ovoEnabled = false,
            danaEnabled = false,
            gopayEnabled = false
        ).toDomain()
}
