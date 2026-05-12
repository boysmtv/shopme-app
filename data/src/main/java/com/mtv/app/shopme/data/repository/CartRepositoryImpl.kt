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
import com.mtv.app.shopme.data.remote.datasource.CartRemoteDataSource
import com.mtv.app.shopme.domain.param.CartAddParam
import com.mtv.app.shopme.domain.param.CartClearByCafeParam
import com.mtv.app.shopme.domain.param.CartQuantityParam
import com.mtv.app.shopme.domain.param.CreateOrderParam
import com.mtv.app.shopme.domain.param.VerifyPinParam
import com.mtv.app.shopme.domain.repository.CartRepository
import com.mtv.based.core.network.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.builtins.ListSerializer
import com.mtv.app.shopme.data.remote.response.CartItemResponse
import com.mtv.app.shopme.data.utils.PayloadCacheStore

class CartRepositoryImpl @Inject constructor(
    private val remote: CartRemoteDataSource,
    private val resultFlow: ResultFlowFactory,
    private val homeDao: HomeDao,
    private val errorMapper: ErrorMapper
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
        resultFlow.createUnit {
            remote.addCart(param)
            PayloadCacheStore.clear(homeDao, CART_CACHE_KEY)
        }

    override fun updateQuantity(param: CartQuantityParam) =
        resultFlow.createUnit {
            remote.updateQuantity(param)
            PayloadCacheStore.clear(homeDao, CART_CACHE_KEY)
        }

    override fun clearCart() =
        resultFlow.createUnit {
            remote.clearCart()
            PayloadCacheStore.clear(homeDao, CART_CACHE_KEY)
        }

    override fun clearCartByCafe(param: CartClearByCafeParam) =
        resultFlow.createUnit {
            remote.clearCartByCafe(param)
            PayloadCacheStore.clear(homeDao, CART_CACHE_KEY)
        }

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
}
