/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodDetailRemoteDataSource.kt
 *
 * Last modified by Dedy Wijaya on 27/03/26 22.39
 */

package com.mtv.app.shopme.data.remote.datasource

import com.mtv.app.shopme.data.base.BaseRemoteDataSource
import com.mtv.app.shopme.data.mapper.toRequest
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.utils.requireData
import com.mtv.app.shopme.domain.param.CartAddParam
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject

class FoodDetailRemoteDataSource @Inject constructor(
    network: NetworkRepository
) : BaseRemoteDataSource(network) {

    suspend fun getFoodDetail(id: String) =
        request<ApiResponse<FoodResponse>>(
            endpoint = ApiEndPoint.Foods.Detail(id)
        ).requireData()

    suspend fun getSimilarFoods(cafeId: String) =
        request<ApiResponse<List<FoodResponse>>>(
            endpoint = ApiEndPoint.Foods.GetSimilarByCafe(cafeId)
        ).requireData()

    suspend fun addToCart(param: CartAddParam) =
        request<ApiResponse<Unit>>(
            endpoint = ApiEndPoint.Cart.Add,
            body = param.toRequest()
        ).requireData()

}