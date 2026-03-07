/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 11.55
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.core.provider.based.BaseUseCase
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.CartItemResponse
import com.mtv.based.core.network.di.IoDispatcher
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class CartItemUseCase @Inject constructor(
    private val repository: NetworkRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<Unit, ApiResponse<CartItemResponse>>(dispatcher) {

    override suspend fun execute(param: Unit) = repository.request<ApiResponse<CartItemResponse>>(
        endpoint = ApiEndPoint.GetCart,
    )

}