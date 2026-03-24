/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 00.59
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.common.isExpired
import com.mtv.app.shopme.common.utils.network.networkBoundResource
import com.mtv.app.shopme.core.cache.CachePolicy
import com.mtv.app.shopme.core.data.defaultRetryPolicy
import com.mtv.app.shopme.core.error.ErrorMapper
import com.mtv.app.shopme.data.local.home.HomeLocalDataSource
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.remote.home.HomeRemoteDataSource
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.Food
import com.mtv.app.shopme.domain.repository.HomeRepository
import com.mtv.based.core.network.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class HomeRepositoryImpl @Inject constructor(
    private val local: HomeLocalDataSource,
    private val remote: HomeRemoteDataSource,
    private val cachePolicy: CachePolicy,
    private val errorMapper: ErrorMapper
) : HomeRepository {

    override fun getCustomer(): Flow<Resource<Customer>> =
        networkBoundResource(
            query = { local.getCustomer() },
            fetch = { remote.getCustomer() },
            saveFetchResult = {
                local.saveCustomer(
                    it.toDomain().copy(
                        updatedAt = System.currentTimeMillis()
                    )
                )
            },
            shouldFetch = { data ->
                data == null || isExpired(
                    data.updatedAt,
                    ttl = cachePolicy.customerTtl
                )
            },
            errorMapper = errorMapper::map,
            retryPolicy = { block ->
                defaultRetryPolicy { block() }
            }
        )

    override fun getFoods(): Flow<Resource<List<Food>>> =
        networkBoundResource(
            query = { local.getFoods() },
            fetch = { remote.getFoods() },
            saveFetchResult = { list ->
                local.saveFoods(
                    list.map {
                        it.toDomain().copy(
                            updatedAt = System.currentTimeMillis()
                        )
                    }
                )
            },

            shouldFetch = { data ->
                when {
                    data.isNullOrEmpty() -> true
                    else -> {
                        val oldest = data.minOfOrNull { it.updatedAt } ?: 0L
                        isExpired(oldest, ttl = cachePolicy.foodsTtl)
                    }
                }
            },
            errorMapper = errorMapper::map,
            retryPolicy = { block ->
                defaultRetryPolicy { block() }
            }
        )
}