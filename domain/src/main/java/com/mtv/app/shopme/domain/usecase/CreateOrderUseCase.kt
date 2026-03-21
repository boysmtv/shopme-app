/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: VerifyPinUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 13.33
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.based.core.provider.based.BaseUseCase
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.request.CreateOrderRequest
import com.mtv.app.shopme.data.remote.request.VerifyPinRequest
import com.mtv.app.shopme.data.remote.response.VerifyPinResponse
import com.mtv.based.core.network.di.IoDispatcher
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class CreateOrderUseCase @Inject constructor(
    private val repository: NetworkRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<CreateOrderRequest, ApiResponse<Unit>>(dispatcher) {

    override suspend fun execute(param: CreateOrderRequest) = repository.request<ApiResponse<Unit>>(
        endpoint = ApiEndPoint.Order.Create,
        body = param
    )

}