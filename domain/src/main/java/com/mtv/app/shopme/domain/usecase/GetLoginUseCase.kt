/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: SplashUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 14.12
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.based.core.provider.based.BaseUseCase
import com.mtv.app.shopme.data.remote.api.ApiResponse
import com.mtv.app.shopme.data.remote.api.ApiEndPoint
import com.mtv.app.shopme.data.remote.request.LoginRequest
import com.mtv.app.shopme.data.remote.response.LoginResponse
import com.mtv.based.core.network.di.IoDispatcher
import com.mtv.based.core.network.repository.NetworkRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class GetLoginUseCase @Inject constructor(
    private val repository: NetworkRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<LoginRequest, ApiResponse<LoginResponse>>(dispatcher) {

    override suspend fun execute(param: LoginRequest) = repository.request<ApiResponse<LoginResponse>>(
        endpoint = ApiEndPoint.Auth.Login,
        body = param
    )

}