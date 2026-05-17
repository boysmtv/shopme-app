/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 01/04/26 10.43
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.database.dao.HomeDao
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toCacheEntity
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.mapper.toEntity
import com.mtv.app.shopme.data.mapper.toSearchDomain
import com.mtv.app.shopme.data.remote.datasource.FoodRemoteDataSource
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.utils.PayloadCacheStore
import com.mtv.app.shopme.domain.model.PagedData
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.model.SearchFood
import com.mtv.app.shopme.domain.param.FoodUpsertParam
import com.mtv.app.shopme.domain.param.SearchParam
import com.mtv.app.shopme.domain.repository.FoodRepository
import com.mtv.based.core.network.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FoodRepositoryImpl @Inject constructor(
    private val remote: FoodRemoteDataSource,
    private val resultFlow: ResultFlowFactory,
    private val homeDao: HomeDao,
    private val errorMapper: ErrorMapper
) : FoodRepository {

    override fun getFoods() =
        flow {
            emit(Resource.Loading)
            val cached = homeDao.getFoodsOnce().map { it.toDomain() }
            if (cached.isNotEmpty()) {
                emit(Resource.Success(cached))
            }

            try {
                val remoteFoods = remote.getFoods()
                val mapped = remoteFoods.map { it.toDomain() }
                homeDao.clearFoods()
                homeDao.insertFoods(mapped.map { it.toEntity() })
                emit(Resource.Success(mapped))
            } catch (throwable: Throwable) {
                if (cached.isEmpty()) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun getFoodsByCafe(id: String) =
        resultFlow.create {
            remote.getFoodsByCafe(id).map { it.toDomain() }
        }

    override fun getFoodsByCafe(
        id: String,
        page: Int,
        size: Int,
        query: String,
        category: FoodCategory?,
        status: FoodStatus?,
        active: Boolean?
    ): Flow<Resource<PagedData<com.mtv.app.shopme.domain.model.Food>>> =
        flow {
            emit(Resource.Loading)
            try {
                val response = remote.getFoodsByCafe(id, page, size, query, category, status, active)
                emit(
                    Resource.Success(
                        PagedData(
                            content = response.content.map { it.toDomain() },
                            page = response.page,
                            last = response.last
                        )
                    )
                )
            } catch (throwable: Throwable) {
                emit(Resource.Error(errorMapper.map(throwable)))
            }
        }.flowOn(Dispatchers.IO)

    override fun getFoodDetail(id: String) =
        flow {
            emit(Resource.Loading)
            val cacheKey = foodDetailCacheKey(id)
            val cached = PayloadCacheStore.read(
                homeDao = homeDao,
                cacheKey = cacheKey,
                serializer = FoodResponse.serializer()
            )
            if (cached != null) {
                emit(Resource.Success(cached.toDomain()))
            }

            try {
                val remoteFood = remote.getFoodDetail(id)
                PayloadCacheStore.write(
                    homeDao = homeDao,
                    cacheKey = cacheKey,
                    serializer = FoodResponse.serializer(),
                    value = remoteFood
                )
                emit(Resource.Success(remoteFood.toDomain()))
            } catch (throwable: Throwable) {
                if (cached == null) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun getSimilarFoods(cafeId: String) =
        resultFlow.create {
            remote.getSimilarFoods(cafeId).map { it.toDomain() }
        }

    override fun searchFoods(request: SearchParam): Flow<Resource<PagedData<SearchFood>>> =
        flow {
            emit(Resource.Loading)
            val useHomeCache = request.name.isBlank() && request.page == 0 && request.sort != "random"
            val cached = if (useHomeCache) {
                homeDao.getFoodsOnce().map { it.toSearchDomain() }
            } else {
                emptyList()
            }

            if (useHomeCache && cached.isNotEmpty()) {
                emit(Resource.Success(PagedData(content = cached, page = 0, last = false)))
            }

            try {
                val response = remote.searchFoods(request)
                val mapped = response.content.map { it.toSearchDomain() }

                if (useHomeCache) {
                    homeDao.clearFoods()
                    homeDao.insertFoods(mapped.map { it.toCacheEntity() })
                }

                emit(
                    Resource.Success(
                        PagedData(
                            content = mapped,
                            page = response.page,
                            last = response.last
                        )
                    )
                )
            } catch (throwable: Throwable) {
                if (cached.isEmpty()) {
                    emit(Resource.Error(errorMapper.map(throwable)))
                }
            }
        }.flowOn(Dispatchers.IO)

    override fun createFood(param: FoodUpsertParam) =
        resultFlow.create {
            remote.createFood(param)
        }

    override fun updateFood(foodId: String, param: FoodUpsertParam) =
        resultFlow.create {
            remote.updateFood(foodId, param)
        }

    override fun deleteFood(foodId: String) =
        resultFlow.create {
            remote.deleteFood(foodId)
        }

    private fun foodDetailCacheKey(id: String) = "food:detail:$id"

}
