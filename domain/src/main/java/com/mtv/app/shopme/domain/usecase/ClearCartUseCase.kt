/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CartQuantityUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 13.05
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.based.core.provider.based.BaseUseCase
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.request.CartQuantityRequest
import com.mtv.based.core.network.di.IoDispatcher
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class ClearCartUseCase @Inject constructor(
    private val repository: NetworkRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<Unit, ApiResponse<Unit>>(dispatcher) {

    override suspend fun execute(param: Unit) = repository.request<ApiResponse<Unit>>(
        endpoint = ApiEndPoint.Cart.Clear,
    )

}