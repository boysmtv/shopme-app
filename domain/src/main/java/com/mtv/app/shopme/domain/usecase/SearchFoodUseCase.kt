/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 11.55
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.based.core.provider.based.BaseUseCase
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.request.SearchFoodRequest
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.app.shopme.data.remote.response.PageResponse
import com.mtv.based.core.network.di.IoDispatcher
import com.mtv.based.core.network.model.RequestOptions
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class SearchFoodUseCase @Inject constructor(
    private val repository: NetworkRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<SearchFoodRequest, ApiResponse<PageResponse<FoodResponse>>>(dispatcher) {

    override suspend fun execute(param: SearchFoodRequest) =
        repository.request<ApiResponse<PageResponse<FoodResponse>>>(
            endpoint = ApiEndPoint.Foods.Search,
            options = RequestOptions(
                query = mapOf(
                    "name" to param.name,
                    "page" to param.page.toString(),
                    "size" to param.size.toString(),
                    "sort" to param.sort
                )
            )
        )

}