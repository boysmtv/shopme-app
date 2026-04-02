/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 01/04/26 10.43
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.toDomain
import com.mtv.app.shopme.data.mapper.toSearchDomain
import com.mtv.app.shopme.data.remote.datasource.FoodRemoteDataSource
import com.mtv.app.shopme.data.remote.datasource.SearchRemoteDataSource
import com.mtv.app.shopme.domain.model.PagedData
import com.mtv.app.shopme.domain.model.SearchFood
import com.mtv.app.shopme.domain.param.SearchParam
import com.mtv.app.shopme.domain.repository.FoodRepository
import com.mtv.based.core.network.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FoodRepositoryImpl @Inject constructor(
    private val remote: FoodRemoteDataSource,
    private val resultFlow: ResultFlowFactory
) : FoodRepository {

    override fun getFoods() =
        resultFlow.create {
            remote.getFoods().map { it.toDomain() }
        }

    override fun getFoodsByCafe(id: String) =
        resultFlow.create {
            remote.getFoodsByCafe(id).map { it.toDomain() }
        }

    override fun getFoodDetail(id: String) =
        resultFlow.create {
            remote.getFoodDetail(id).toDomain()
        }

    override fun getSimilarFoods(cafeId: String) =
        resultFlow.create {
            remote.getSimilarFoods(cafeId).map { it.toDomain() }
        }

    override fun searchFoods(request: SearchParam): Flow<Resource<PagedData<SearchFood>>> =
        resultFlow.create {
            val response = remote.searchFoods(request)

            PagedData(
                content = response.content.map { it.toSearchDomain() },
                page = response.page,
                last = response.last
            )
        }

}