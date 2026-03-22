/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: FoodDetailUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 11.39
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.based.core.provider.based.BaseUseCase
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.FoodResponse
import com.mtv.based.core.network.di.IoDispatcher
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class FoodDetailUseCase @Inject constructor(
    private val repository: NetworkRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<String, ApiResponse<FoodResponse>>(dispatcher) {

    override suspend fun execute(param: String) = repository.request<ApiResponse<FoodResponse>>(
        endpoint = ApiEndPoint.Foods.Detail(param),
    )

}