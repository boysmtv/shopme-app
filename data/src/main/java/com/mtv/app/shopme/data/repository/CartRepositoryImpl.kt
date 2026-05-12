/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 25/03/26 12.24
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.mapper.toRequest
import com.mtv.app.shopme.data.remote.datasource.CartRemoteDataSource
import com.mtv.app.shopme.data.remote.request.CartQuantityRequest
import com.mtv.app.shopme.data.remote.request.FoodAddToCartRequest
import com.mtv.app.shopme.data.remote.response.CartItemResponse
import com.mtv.app.shopme.data.remote.response.CartItemVariantResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.domain.param.CartAddParam
import com.mtv.app.shopme.domain.param.CartClearByCafeParam
import com.mtv.app.shopme.domain.param.CartQuantityParam
import com.mtv.app.shopme.domain.param.CreateOrderParam
import com.mtv.app.shopme.domain.param.VerifyPinParam
import com.mtv.app.shopme.domain.repository.CartRepository
import com.mtv.app.shopme.data.sync.OfflineMutationSyncManager
import com.mtv.app.shopme.data.sync.PendingMutationAction
import com.mtv.app.shopme.data.utils.PayloadCacheStore
import com.mtv.based.core.network.utils.Resource
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.serialization.builtins.ListSerializer
import java.math.BigDecimal

class CartRepositoryImpl @Inject constructor(
    private val remote: CartRemoteDataSource,
    private val resultFlow: ResultFlowFactory,
    private val homeDao: HomeDao,
    private val errorMapper: ErrorMapper,
    private val syncManager: OfflineMutationSyncManager
) : CartRepository {

    override fun getCart() =
        flow {
            emit(Resource.Loading)
            val cached = PayloadCacheStore.read(
                homeDao = homeDao,
                cacheKey = CART_CACHE_KEY,
                serializer = ListSerializer(CartItemResponse.serializer())
            ).orEmpty()
            if (cached.isNotEmpty()) {
                emit(Resource.Success(cached.map { it.toDomain() }))
            }

            try {
                val remoteCart = remote.getCart()
                PayloadCacheStore.write(
                    homeDao = homeDao,
                    cacheKey = CART_CACHE_KEY,
                    serializer = ListSerializer(CartItemResponse.serializer()),
                    value = remoteCart
                )
                emit(Resource.Success(remoteCart.map { it.toDomain() }))
            } catch (throwable: Throwable) {
                if (cached.isEmpty()) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun addCart(param: CartAddParam) =
        flow {
            emit(Resource.Loading)
            try {
                remote.addCart(param)
                PayloadCacheStore.clear(homeDao, CART_CACHE_KEY)
                emit(Resource.Success(Unit))
            } catch (throwable: Throwable) {
                if (throwable.isRetryableOfflineWrite()) {
                    syncManager.enqueue(
                        PendingMutationAction.CartAdd(param.toRequest())
                    )
                    patchCartAdd(param)
                    emit(Resource.Success(Unit))
                } else {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun updateQuantity(param: CartQuantityParam) =
        flow {
            emit(Resource.Loading)
            try {
                remote.updateQuantity(param)
                patchCartQuantity(param.cartId, param.quantity)
                emit(Resource.Success(Unit))
            } catch (throwable: Throwable) {
                if (throwable.isRetryableOfflineWrite()) {
                    syncManager.enqueue(
                        PendingMutationAction.CartQuantity(
                            cartId = param.cartId,
                            body = CartQuantityRequest(quantity = param.quantity)
                        )
                    )
                    patchCartQuantity(param.cartId, param.quantity)
                    emit(Resource.Success(Unit))
                } else {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun clearCart() =
        flow {
            emit(Resource.Loading)
            try {
                remote.clearCart()
                overwriteCartCache(emptyList())
                emit(Resource.Success(Unit))
            } catch (throwable: Throwable) {
                if (throwable.isRetryableOfflineWrite()) {
                    syncManager.enqueue(PendingMutationAction.CartClear)
                    overwriteCartCache(emptyList())
                    emit(Resource.Success(Unit))
                } else {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun clearCartByCafe(param: CartClearByCafeParam) =
        flow {
            emit(Resource.Loading)
            try {
                remote.clearCartByCafe(param)
                patchCartClearByCafe(param.cafeId)
                emit(Resource.Success(Unit))
            } catch (throwable: Throwable) {
                if (throwable.isRetryableOfflineWrite()) {
                    syncManager.enqueue(PendingMutationAction.CartClearByCafe(param.cafeId))
                    patchCartClearByCafe(param.cafeId)
                    emit(Resource.Success(Unit))
                } else {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun createOrder(param: CreateOrderParam) =
        resultFlow.createUnit {
            remote.createOrder(param)
            PayloadCacheStore.clear(homeDao, CART_CACHE_KEY)
        }

    override fun verifyPin(param: VerifyPinParam) =
        resultFlow.createUnit {
            remote.verifyPin(param)
        }

    override fun getSessionToken() =
        resultFlow.create {
            remote.getSessionToken().toDomain()
        }

    companion object {
        private const val CART_CACHE_KEY = "cart:list"
    }

    private suspend fun patchCartQuantity(cartId: String, quantity: Int) {
        val cached = readCartCache()
        if (cached.isEmpty()) return
        overwriteCartCache(
            cached.map {
                if (it.id == cartId) {
                    it.copy(quantity = quantity)
                } else {
                    it
                }
            }
        )
    }

    private suspend fun patchCartClearByCafe(cafeId: String) {
        val cached = readCartCache()
        if (cached.isEmpty()) return
        overwriteCartCache(cached.filterNot { it.cafeId == cafeId })
    }

    private suspend fun patchCartAdd(param: CartAddParam) {
        val cached = readCartCache()
        val foodDetail = PayloadCacheStore.read(
            homeDao = homeDao,
            cacheKey = foodDetailCacheKey(param.foodId),
            serializer = FoodResponse.serializer()
        ) ?: return
        val customerId = homeDao.getCustomerOnce()?.id.orEmpty()
        val variantPayload = param.variants.orEmpty().mapNotNull { selected ->
            val variant = foodDetail.variants.firstOrNull { it.id == selected.variantId } ?: return@mapNotNull null
            val option = variant.options.firstOrNull { it.id == selected.optionId } ?: return@mapNotNull null
            CartItemVariantResponse(
                variantId = variant.id,
                variantName = variant.name,
                optionId = option.id,
                optionName = option.name,
                price = option.price
            )
        }
        val normalizedVariants = variantPayload.sortedBy { "${it.variantId}:${it.optionId}" }
        val existingIndex = cached.indexOfFirst {
            it.foodId == param.foodId &&
                it.notes.orEmpty() == param.note &&
                it.variants.orEmpty()
                    .sortedBy { item -> "${item.variantId}:${item.optionId}" } == normalizedVariants
        }
        val variantSurcharge = normalizedVariants.fold(BigDecimal.ZERO) { acc, item -> acc + item.price }

        if (existingIndex >= 0) {
            val updated = cached.toMutableList()
            val current = updated[existingIndex]
            updated[existingIndex] = current.copy(quantity = current.quantity + param.quantity)
            overwriteCartCache(updated)
            return
        }

        val optimisticItem = CartItemResponse(
            id = "pending:${System.currentTimeMillis()}",
            name = foodDetail.name,
            image = foodDetail.images.firstOrNull(),
            quantity = param.quantity,
            notes = param.note.ifBlank { null },
            cafeId = foodDetail.cafeId,
            cafeName = foodDetail.cafeName.orEmpty(),
            foodId = foodDetail.id,
            customerId = customerId,
            price = foodDetail.price + variantSurcharge,
            variants = normalizedVariants.ifEmpty { null }
        )
        overwriteCartCache(cached + optimisticItem)
    }

    private suspend fun readCartCache(): List<CartItemResponse> =
        PayloadCacheStore.read(
            homeDao = homeDao,
            cacheKey = CART_CACHE_KEY,
            serializer = ListSerializer(CartItemResponse.serializer())
        ).orEmpty()

    private suspend fun overwriteCartCache(items: List<CartItemResponse>) {
        PayloadCacheStore.write(
            homeDao = homeDao,
            cacheKey = CART_CACHE_KEY,
            serializer = ListSerializer(CartItemResponse.serializer()),
            value = items
        )
    }

    private fun Throwable.isRetryableOfflineWrite(): Boolean =
        this is IOException || this is TimeoutCancellationException

    private fun foodDetailCacheKey(id: String) = "food:detail:$id"
}
