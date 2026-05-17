/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodRemoteDataSource.kt
 *
 * Last modified by Dedy Wijaya on 01/04/26 10.45
 */

package com.mtv.app.shopme.data.remote.datasource

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.mapper.toRequest
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.remote.response.PageResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.app.shopme.domain.model.FoodCategory
import com.mtv.app.shopme.domain.model.FoodStatus
import com.mtv.app.shopme.domain.param.FoodUpsertParam
import com.mtv.app.shopme.domain.param.SearchParam
import com.mtv.based.core.network.model.RequestOptions
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class FoodRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun getFoods() = request<ApiResponse<List<FoodResponse>>>(
        endpoint = ApiEndPoint.Foods.GetAll
    ).requireData()

    suspend fun getFoodsByCafe(id: String) = request<ApiResponse<List<FoodResponse>>>(
        endpoint = ApiEndPoint.Foods.GetByCafeId(id)
    ).requireData()

    suspend fun getFoodsByCafe(
        id: String,
        page: Int,
        size: Int,
        query: String = "",
        category: FoodCategory? = null,
        status: FoodStatus? = null,
        active: Boolean? = null
    ) =
        request<ApiResponse<PageResponse<FoodResponse>>>(
            endpoint = ApiEndPoint.Foods.GetByCafeIdPage(id),
            options = RequestOptions(
                query = buildMap {
                    put("page", page.toString())
                    put("size", size.toString())
                    if (query.isNotBlank()) put("q", query.trim())
                    category?.let { put("category", it.name) }
                    status?.let { put("status", it.name) }
                    active?.let { put("active", it.toString()) }
                }
            )
        ).requireData()

    suspend fun getFoodDetail(id: String) =
        request<ApiResponse<FoodResponse>>(
            endpoint = ApiEndPoint.Foods.Detail(id)
        ).requireData()

    suspend fun createFood(param: FoodUpsertParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Foods.Create,
            body = param.toRequest()
        ).requireData()

    suspend fun updateFood(foodId: String, param: FoodUpsertParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Foods.Update(foodId),
            body = param.toRequest()
        ).requireData()

    suspend fun deleteFood(foodId: String) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Foods.Delete(foodId)
        ).requireData()

    suspend fun getSimilarFoods(cafeId: String) =
        request<ApiResponse<List<FoodResponse>>>(
            endpoint = ApiEndPoint.Foods.GetSimilarByCafe(cafeId)
        ).requireData()


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
