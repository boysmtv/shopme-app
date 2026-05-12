/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CafeRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 28/03/26 22.32
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.datasource.CafeRemoteDataSource
import com.mtv.app.shopme.data.remote.response.CafeAddressResponse
import com.mtv.app.shopme.data.remote.response.CafeResponse
import com.mtv.app.shopme.data.utils.PayloadCacheStore
import com.mtv.app.shopme.domain.param.CafeAddParam
import com.mtv.app.shopme.domain.param.CafeAddressUpsertParam
import com.mtv.app.shopme.domain.repository.CafeRepository
import com.mtv.based.core.network.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CafeRepositoryImpl @Inject constructor(
    private val remote: CafeRemoteDataSource,
    private val resultFlow: ResultFlowFactory,
    private val homeDao: HomeDao,
    private val errorMapper: ErrorMapper
) : CafeRepository {

    override fun getCafe(id: String) =
        flow {
            emit(Resource.Loading)
            val cacheKey = cafeCacheKey(id)
            val cached = PayloadCacheStore.read(
                homeDao = homeDao,
                cacheKey = cacheKey,
                serializer = CafeResponse.serializer()
            )
            if (cached != null) {
                emit(Resource.Success(cached.toDomain()))
            }

            try {
                val remoteCafe = remote.getCafe(id)
                PayloadCacheStore.write(
                    homeDao = homeDao,
                    cacheKey = cacheKey,
                    serializer = CafeResponse.serializer(),
                    value = remoteCafe
                )
                emit(Resource.Success(remoteCafe.toDomain()))
            } catch (throwable: Throwable) {
                if (cached == null) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun createCafe(param: CafeAddParam) =
        resultFlow.create {
            remote.createCafe(param)
        }

    override fun updateCafe(id: String, param: CafeAddParam) =
        resultFlow.create {
            remote.updateCafe(id, param)
            PayloadCacheStore.clear(homeDao, cafeCacheKey(id))
        }

    override fun getCafeAddress(id: String) =
        flow {
            emit(Resource.Loading)
            val cacheKey = cafeAddressCacheKey(id)
            val cached = PayloadCacheStore.read(
                homeDao = homeDao,
                cacheKey = cacheKey,
                serializer = CafeAddressResponse.serializer()
            )
            if (cached != null) {
                emit(Resource.Success(cached.toDomain()))
            }

            try {
                val remoteAddress = remote.getCafeAddress(id)
                PayloadCacheStore.write(
                    homeDao = homeDao,
                    cacheKey = cacheKey,
                    serializer = CafeAddressResponse.serializer(),
                    value = remoteAddress
                )
                emit(Resource.Success(remoteAddress.toDomain()))
            } catch (throwable: Throwable) {
                if (cached == null) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun upsertCafeAddress(param: CafeAddressUpsertParam) =
        resultFlow.create {
            remote.upsertCafeAddress(param)
            PayloadCacheStore.clear(homeDao, cafeAddressCacheKey(param.cafeId))
        }

    private fun cafeCacheKey(id: String) = "cafe:detail:$id"

    private fun cafeAddressCacheKey(id: String) = "cafe:address:$id"
}
