/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: GetCafeUseCase.kt
 *
 * Last modified by Dedy Wijaya on 18/03/26 01.29
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.based.core.provider.based.BaseUseCase
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.response.CafeResponse
import com.mtv.based.core.network.di.IoDispatcher
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class GetCafeUseCase @Inject constructor(
    private val repository: NetworkRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<String, ApiResponse<CafeResponse>>(dispatcher) {

    override suspend fun execute(param: String) = repository.request<ApiResponse<CafeResponse>>(
        endpoint = ApiEndPoint.Cafe.Detail(param),
    )
}