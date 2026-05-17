/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SearchRemoteDataSource.kt
 *
 * Last modified by Dedy Wijaya on 26/03/26 13.50
 */

package com.mtv.app.shopme.data.remote.datasource

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.remote.response.PageResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.app.shopme.domain.param.SearchParam
import com.mtv.based.core.network.model.RequestOptions
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class SearchRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun searchFoods(param: SearchParam) = request<ApiResponse<PageResponse<FoodResponse>>>(
            endpoint = ApiEndPoint.Foods.Search,
            options = RequestOptions(
                query = mapOf(
                    "name" to param.name,
                    "page" to param.page.toString(),
                    "size" to param.size.toString(),
                    "sort" to param.sort,
                    "seed" to param.seed
                )
            )
        ).requireData()

}
