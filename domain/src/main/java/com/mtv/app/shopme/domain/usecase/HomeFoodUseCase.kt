/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HomeFoodUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 11.36
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

class HomeFoodUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val repository: NetworkRepository,
) : BaseUseCase<Unit, ApiResponse<List<FoodResponse>>>(dispatcher) {

    override suspend fun execute(param: Unit) = repository.request<ApiResponse<List<FoodResponse>>>(
        endpoint = ApiEndPoint.Foods.GetAll
    )
}
