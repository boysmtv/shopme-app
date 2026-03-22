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
import com.mtv.app.shopme.data.remote.request.FoodAddToCartRequest
import com.mtv.based.core.network.di.IoDispatcher
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class FoodAddToCartUseCase @Inject constructor(
    private val repository: NetworkRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<FoodAddToCartRequest, ApiResponse<Unit>>(dispatcher) {

    override suspend fun execute(param: FoodAddToCartRequest) = repository.request<ApiResponse<Unit>>(
        endpoint = ApiEndPoint.Cart.Add,
        body = param
    )

}