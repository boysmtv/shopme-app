/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchRepositoryImpl.kt
 *
 * Last modified by Dedy Wijaya on 26/03/26 13.58
 */

package com.mtv.app.shopme.data.repository

import com.mtv.app.shopme.core.utils.ResultFlowFactory
import com.mtv.app.shopme.data.mapper.param.toSearchDomain
import com.mtv.app.shopme.data.remote.SearchRemoteDataSource
import com.mtv.app.shopme.domain.model.PagedData
import com.mtv.app.shopme.domain.model.SearchFood
import com.mtv.app.shopme.domain.model.param.SearchParam
import com.mtv.app.shopme.domain.repository.SearchRepository
import com.mtv.based.core.network.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl @Inject constructor(
    private val remote: SearchRemoteDataSource,
    private val resultFlow: ResultFlowFactory
) : SearchRepository {

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